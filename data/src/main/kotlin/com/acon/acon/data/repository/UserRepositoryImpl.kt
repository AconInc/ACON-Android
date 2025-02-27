package com.acon.acon.data.repository

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude
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
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
): UserRepository {
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


            AconAmplitude.setUserProperty(loginResponse.externalUUID)
            AconAmplitude.setUserId(loginResponse.externalUUID)

            AconTestAmplitude.setUserProperty(loginResponse.externalUUID)
            AconTestAmplitude.setUserId(loginResponse.externalUUID)

            loginResponse.toVerificationStatus()
        }.onFailure {}
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