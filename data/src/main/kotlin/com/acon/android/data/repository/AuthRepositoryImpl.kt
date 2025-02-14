package com.acon.android.data.repository

import com.acon.android.data.datasource.local.TokenLocalDataSource
import com.acon.android.data.datasource.remote.AuthRemoteDataSource
import com.acon.android.data.dto.request.GoogleTokenRequest
import com.acon.android.data.error.runCatchingWith
//import com.acon.android.domain.error.auth.PostLoginError
import com.acon.android.domain.repository.AuthRepository
import com.acon.android.domain.type.SocialType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
): AuthRepository {

    private val _isLogin = MutableStateFlow(false)
    private val isLogin: StateFlow<Boolean> = _isLogin.asStateFlow()

    override suspend fun postLogin(
        socialType: SocialType,
        idToken: String
    ): Result<Unit> {
        return runCatchingWith() {
            val googleLoginResponse = authRemoteDataSource.postLogin(
                GoogleTokenRequest(
                    socialType = socialType,
                    idToken = idToken
                )
            )
            googleLoginResponse.accessToken?.let { tokenLocalDataSource.saveAccessToken(it) }
            googleLoginResponse.refreshToken?.let { tokenLocalDataSource.saveRefreshToken(it) }

            _isLogin.value = true
        }.onFailure {
            _isLogin.value = false
        }
    }

    override fun getLoginState(): StateFlow<Boolean> {
        return isLogin
    }

}