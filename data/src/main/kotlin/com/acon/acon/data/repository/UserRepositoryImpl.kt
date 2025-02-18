package com.acon.acon.data.repository

import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.datasource.remote.UserRemoteDataSource
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.LoginRequest
import com.acon.acon.data.dto.request.LogoutRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.user.PostLoginError
import com.acon.acon.domain.error.user.PostLogoutError
import com.acon.acon.domain.model.user.VerificationStatus
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
    ): Result<VerificationStatus> {
        return runCatchingWith(*PostLoginError.createErrorInstances()) {
            val loginResponse = userRemoteDataSource.postLogin(
                LoginRequest(
                    socialType = socialType,
                    idToken = idToken
                )
            )
            loginResponse.accessToken?.let { tokenLocalDataSource.saveAccessToken(it) }
            loginResponse.refreshToken?.let { tokenLocalDataSource.saveRefreshToken(it) }


            _isLogin.value = true

            loginResponse.toVerificationStatus()
        }.onFailure {
            _isLogin.value = false
        }
    }

    override fun getLoginState(): StateFlow<Boolean> {
        return isLogin
    }

    override fun updateLoginState(loginState: Boolean) {
        _isLogin.value = loginState
    }

    override suspend fun postLogout(refreshToken: String): Result<Unit> {
        return runCatchingWith(*PostLogoutError.createErrorInstances()) {
            userRemoteDataSource.postLogout(
                LogoutRequest(
                    refreshToken = refreshToken
                )
            )
            tokenLocalDataSource.removeAllTokens()
        }
    }

    override suspend fun postDeleteAccount(reason: String, refreshToken: String): Result<Unit> {
        return runCatchingWith() {
            userRemoteDataSource.postDeleteAccount(
                DeleteAccountRequest(
                    reason = reason,
                    refreshToken = refreshToken
                )
            )
            tokenLocalDataSource.removeAllTokens()
        }
    }
}