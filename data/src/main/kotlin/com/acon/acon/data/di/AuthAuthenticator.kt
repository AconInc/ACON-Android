package com.acon.acon.data.di

import android.util.Log
import com.acon.acon.data.BuildConfig
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
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val reissueTokenApi: ReissueTokenApi,
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "[authenticate] 호출됨. 요청 URL: ${response.request.url}")
        }

        mutex.withLock {
            val currentRefreshToken = tokenLocalDataSource.getRefreshToken() ?: ""
            if (currentRefreshToken.isEmpty()) {
                Log.e(TAG, "저장된 Refresh Token이 없음. 토큰 제거 후 로그인 화면으로 이동")
                tokenLocalDataSource.removeAllTokens()
                goToSignInScreen()
                return@withLock null
            }

            val result = runCatching {
                reissueTokenApi.postRefresh(RefreshRequest(currentRefreshToken))
            }

            when {
                result.isSuccess -> {
                    if(BuildConfig.DEBUG) { Log.d(TAG, "토큰 재발급 요청 성공") }
                    val tokenResponse = result.getOrNull()
                    if (tokenResponse == null) {
                        tokenLocalDataSource.removeAllTokens()
                        goToSignInScreen()
                        return@withLock null
                    }

                    val tokenBody = tokenResponse.toRefreshToken()
                    if (tokenBody.accessToken.isNullOrEmpty() || tokenBody.refreshToken.isNullOrEmpty()) {
                        if(BuildConfig.DEBUG) {
                            Log.e(TAG, "토큰이 비어 있음. 토큰 제거 후 로그인 화면으로 이동")
                        }
                        tokenLocalDataSource.removeAllTokens()
                        goToSignInScreen()
                        return@withLock null
                    }

                    tokenBody.accessToken?.let { tokenLocalDataSource.saveAccessToken(it) }
                    tokenBody.refreshToken?.let { tokenLocalDataSource.saveRefreshToken(it) }

                    if(BuildConfig.DEBUG) {
                        Log.d(TAG, "[authenticate] 새 액세스 토큰으로 요청 재시도")
                    }
                    return@withLock response.request.newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer ${tokenBody.accessToken}")
                        .build()
                }

                else -> {
                    if(BuildConfig.DEBUG) {
                        Log.e(TAG, "토큰 재발급 실패. 토큰 제거 후 로그인 화면으로 이동")
                    }
                    tokenLocalDataSource.removeAllTokens()
                    goToSignInScreen()
                    return@withLock null
                }
            }
        }
    }

    private fun goToSignInScreen() {

    }

    companion object {
        const val TAG = "AuthAuthenticator"
    }
}