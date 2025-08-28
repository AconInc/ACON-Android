package com.acon.core.data.authentication

import android.content.Context
import com.acon.acon.core.launcher.AppLauncher
import com.acon.core.data.api.remote.noauth.UserNoAuthApi
import com.acon.core.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.core.data.dto.request.ReissueRequest
import com.acon.core.data.dto.request.SignOutRequest
import com.acon.core.data.error.runCatchingWith
import com.acon.core.data.session.SessionHandler
import com.acon.acon.domain.error.user.ReissueError
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import okio.Buffer
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val context: Context,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val sessionHandler: SessionHandler,
    private val userNoAuthApi: UserNoAuthApi,
    private val appLauncher: AppLauncher
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {

        mutex.withLock {
            val currentRefreshToken = tokenLocalDataSource.getRefreshToken()

            if (currentRefreshToken.isNullOrEmpty()) {
                startNewTask()
                return@withLock null
            }

            runCatchingWith(ReissueError()) {
                userNoAuthApi.reissueToken(ReissueRequest(currentRefreshToken))
            }.onSuccess { tokenResponse ->
                if (tokenResponse.accessToken == null) {
                    startNewTask()
                    return@withLock null
                }

                val newAccessToken = tokenResponse.accessToken
                val newRefreshToken = tokenResponse.refreshToken

                tokenLocalDataSource.saveAccessToken(newAccessToken)

                val newRequestBuilder = response.request.newBuilder()
                    .removeHeader("Authorization")
                    .header("Authorization", "Bearer $newAccessToken")

                newRefreshToken?.also {
                    tokenLocalDataSource.saveRefreshToken(it)

                    if (response.request.url.toString().contains("auth/logout")) {
                        val signOutRequestJson = Json.encodeToString(SignOutRequest(newRefreshToken))
                        val newRequestBody = signOutRequestJson.toRequestBody("application/json".toMediaTypeOrNull())
                        newRequestBuilder.apply { method(response.request.method, newRequestBody) }
                    }
                    else if(response.request.url.toString().contains("members/withdrawal")) {
                        val reason = extractReasonFromRequestBody(response.request.body)

                        val deleteAccountRequestJson = Json.encodeToString(DeleteAccountRequest(reason, newRefreshToken))
                        val newRequestBody = deleteAccountRequestJson.toRequestBody("application/json".toMediaTypeOrNull())
                        newRequestBuilder.apply { method(response.request.method, newRequestBody) }
                    }
                }

                return@withLock newRequestBuilder.build()
            }

            startNewTask()
            return@withLock null
        }
    }

    private fun extractReasonFromRequestBody(requestBody: RequestBody?): String {
        return try {
            val buffer = Buffer()
            requestBody?.writeTo(buffer)
            val requestBodyString = buffer.readUtf8()

            val deleteAccountRequest = Json.decodeFromString<DeleteAccountRequest>(requestBodyString)
            deleteAccountRequest.reason
        } catch (e: Exception) {
            ""
        }
    }

    private suspend fun startNewTask() {
        sessionHandler.clearSession()
        appLauncher.restartApp(context)
    }
}