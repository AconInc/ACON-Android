package com.acon.acon.data.di

import android.content.Context
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.dto.request.RefreshRequest
import com.acon.acon.data.remote.ReissueTokenApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val context: Context,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val reissueTokenApi: ReissueTokenApi,
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        mutex.withLock {
            val currentRefreshToken = tokenLocalDataSource.getRefreshToken() ?: ""

            val newResponse = runCatching {
                reissueTokenApi.postRefresh(RefreshRequest(currentRefreshToken))
            }.onSuccess {
                    tokenLocalDataSource.removeRefreshToken()
                    goToLoginActivity() // 로그인 화면으로 이동
                    return@withLock null
            }.onFailure {
                //Timber.e("Refresh 재발급 API 호출 에러 : ${it.message}")
            }.getOrNull()

            val tokenBody = newResponse?.toRefreshToken() ?: run {
                tokenLocalDataSource.removeRefreshToken()
                goToLoginActivity()
                return@withLock null
            }

            tokenBody.accessToken?.let { tokenLocalDataSource.saveAccessToken(it) }
            tokenBody.refreshToken?.let { tokenLocalDataSource.saveRefreshToken(it) }
            response.request.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer ${tokenBody.accessToken}")
                .build()
        }
    }

    private fun goToLoginActivity() {


    }
}