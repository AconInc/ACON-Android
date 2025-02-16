package com.acon.acon.data.repository

import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.datasource.remote.UserRemoteDataSource
import com.acon.acon.data.dto.request.LoginRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.user.PostLoginError
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.SocialType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
): UserRepository {

    private val _isLogin = MutableStateFlow(false)
    private val isLogin: StateFlow<Boolean> = _isLogin.asStateFlow()

    override suspend fun postLogin(
        socialType: SocialType,
        idToken: String
    ): Result<Unit> {
        return runCatchingWith(*PostLoginError.createErrorInstances()) {
            val googleLoginResponse = userRemoteDataSource.postLogin(
                LoginRequest(
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