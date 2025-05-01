package com.acon.acon.data.di

import android.util.Log
import com.acon.acon.data.BuildConfig
import com.acon.acon.data.SessionManager
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.LogoutRequest
import com.acon.acon.data.dto.request.RefreshRequest
import com.acon.acon.data.remote.ReissueTokenApi
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
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val reissueTokenApi: ReissueTokenApi,
    private val sessionManager: SessionManager
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        if(BuildConfig.DEBUG) {
            Timber.tag(TAG).d("[authenticate] 호출됨. 요청 URL: ${response.request.url}")
        }

        mutex.withLock {
            val currentRefreshToken = tokenLocalDataSource.getRefreshToken() ?: ""

            if (currentRefreshToken.isEmpty()) {
                Timber.tag(TAG).e("저장된 Refresh Token이 없음. 토큰 제거 후 로그인 화면으로 이동")
                sessionManager.clearSession()
                goToSignInScreen()
                return@withLock null
            }

            val result = runCatching {
                reissueTokenApi.postRefresh(RefreshRequest(currentRefreshToken))
            }

            when {
                result.isSuccess -> {
                    if(BuildConfig.DEBUG) {
                        Timber.tag(TAG).d("토큰 재발급 요청 성공") }
                    val tokenResponse = result.getOrNull()

                    if (tokenResponse == null) {
                        sessionManager.clearSession()
                        goToSignInScreen()
                        return@withLock null
                    }

                    val tokenBody = tokenResponse.toRefreshToken()
                    if (tokenBody.accessToken.isNullOrEmpty() || tokenBody.refreshToken.isNullOrEmpty()) {
                        if(BuildConfig.DEBUG) {
                            Timber.tag(TAG).e("토큰이 비어 있음. 토큰 제거 후 로그인 화면으로 이동")
                        }
                        sessionManager.clearSession()
                        goToSignInScreen()
                        return@withLock null
                    }

                    tokenBody.accessToken?.let { tokenLocalDataSource.saveAccessToken(it) }
                    tokenBody.refreshToken?.let { tokenLocalDataSource.saveRefreshToken(it) }

                    if(BuildConfig.DEBUG) {
                        Timber.tag(TAG).d("[authenticate] 새 액세스 토큰으로 요청 재시도")
                        Timber.tag(TAG)
                            .d("재실행 요청 정보: ${response.request.method} ${response.request.url}")
                    }

                    return@withLock when {
                        response.request.url.toString().contains("auth/logout") -> {
                            val updatedRefreshToken = tokenLocalDataSource.getRefreshToken() ?: ""

                            val logoutRequestJson = Json.encodeToString(LogoutRequest(updatedRefreshToken))
                            val requestBody: RequestBody = logoutRequestJson.toRequestBody("application/json".toMediaTypeOrNull())

                            response.request.newBuilder()
                                .removeHeader("Authorization")
                                .header("Authorization", "Bearer ${tokenBody.accessToken}")
                                .method(response.request.method, requestBody)
                                .build()
                        }

                        response.request.url.toString().contains("members/withdrawal") -> {
                            val updatedRefreshToken = tokenLocalDataSource.getRefreshToken() ?: ""

                            val originalRequestBody = response.request.body
                            val reason = extractReasonFromRequestBody(originalRequestBody)

                            val updatedRequest = DeleteAccountRequest(reason, updatedRefreshToken)

                            val requestBody: RequestBody = Json.encodeToString(updatedRequest)
                                .toRequestBody("application/json".toMediaTypeOrNull())

                            response.request.newBuilder()
                                .removeHeader("Authorization")
                                .header("Authorization", "Bearer ${tokenBody.accessToken}")
                                .method(response.request.method, requestBody)
                                .build()
                        }

                        else -> {
                            response.request.newBuilder()
                                .removeHeader("Authorization")
                                .header("Authorization", "Bearer ${tokenBody.accessToken}")
                                .build()
                        }
                    }
                }

                else -> {
                    if(BuildConfig.DEBUG) {
                        Timber.tag(TAG).e("토큰 재발급 실패. 토큰 제거 후 로그인 화면으로 이동")
                    }
                    sessionManager.clearSession()
                    goToSignInScreen()
                    return@withLock null
                }
            }
        }
    }

    private fun extractReasonFromRequestBody(originalRequestBody: RequestBody?): String {
        return try {
            val buffer = Buffer()
            originalRequestBody?.writeTo(buffer)
            val requestBodyString = buffer.readUtf8()

            val jsonObject = Json.decodeFromString<Map<String, String>>(requestBodyString)
            jsonObject["reason"] ?: "No reason"
        } catch (e: Exception) {
            "No reason"
        }
    }


    private fun goToSignInScreen() {

    }

    companion object {
        const val TAG = "AuthAuthenticator"
    }
}