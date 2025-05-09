package com.acon.acon.data.repository

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude
import com.acon.acon.data.SessionManager
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
import com.acon.acon.domain.type.UserType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val sessionManager: SessionManager,
): UserRepository {

    override fun getUserType(): Flow<UserType> {
        return sessionManager.getUserType()
    }

    override suspend fun login(
        socialType: SocialType,
        idToken: String
    ): Result<VerificationStatus> {
        return runCatchingWith(*PostLoginError.createErrorInstances()) {
            val loginResponse = userRemoteDataSource.login(
                LoginRequest(
                    socialType = socialType,
                    idToken = idToken
                )
            )

            sessionManager.saveAccessToken(loginResponse.accessToken.orEmpty())
            tokenLocalDataSource.saveRefreshToken(loginResponse.refreshToken.orEmpty())

            loginResponse.hasVerifiedArea.let { tokenLocalDataSource.saveAreaVerification(it) }

            AconAmplitude.setUserProperty(loginResponse.externalUUID)
            AconAmplitude.setUserId(loginResponse.externalUUID)

            AconTestAmplitude.setUserProperty(loginResponse.externalUUID)
            AconTestAmplitude.setUserId(loginResponse.externalUUID)

            loginResponse.toVerificationStatus()
        }
    }

    override suspend fun logout(refreshToken: String): Result<Unit> {
        return runCatchingWith(*PostLogoutError.createErrorInstances()) {
            userRemoteDataSource.logout(
                LogoutRequest(refreshToken = refreshToken)
            )
        }.onSuccess {
            sessionManager.clearSession()
        }
    }

    override suspend fun deleteAccount(reason: String, refreshToken: String): Result<Unit> {
        return runCatchingWith {
            userRemoteDataSource.deleteAccount(
                DeleteAccountRequest(
                    reason = reason,
                    refreshToken = refreshToken
                )
            )
        }.onSuccess {
            sessionManager.clearSession()
        }
    }
}