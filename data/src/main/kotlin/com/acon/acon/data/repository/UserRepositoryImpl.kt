package com.acon.acon.data.repository

import com.acon.acon.core.model.model.user.VerificationStatus
import com.acon.acon.core.model.type.SocialType
import com.acon.acon.core.model.type.UserType
import com.acon.acon.data.SessionManager
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.datasource.remote.UserRemoteDataSource
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.SignInRequest
import com.acon.acon.data.dto.request.SignOutRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.user.PostLogoutError
import com.acon.acon.domain.error.user.PostSignInError
import com.acon.acon.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val sessionManager: SessionManager
) : UserRepository {

    override fun getUserType(): Flow<UserType> {
        return sessionManager.getUserType()
    }

    override suspend fun signIn(
        socialType: SocialType,
        idToken: String
    ): Result<VerificationStatus> {
        return runCatchingWith(*PostSignInError.createErrorInstances()) {
            val signInResponse = userRemoteDataSource.signIn(
                SignInRequest(
                    socialType = socialType,
                    idToken = idToken
                )
            )

            sessionManager.saveAccessToken(signInResponse.accessToken.orEmpty())
            tokenLocalDataSource.saveRefreshToken(signInResponse.refreshToken.orEmpty())

            signInResponse.toVerificationStatus()
        }
    }

    override suspend fun signOut(): Result<Unit> {
        val refreshToken = tokenLocalDataSource.getRefreshToken() ?: ""
        return runCatchingWith(*PostLogoutError.createErrorInstances()) {
            userRemoteDataSource.signOut(
                SignOutRequest(refreshToken = refreshToken)
            )
        }.onSuccess {
            sessionManager.clearSession()
        }
    }

    override suspend fun deleteAccount(reason: String): Result<Unit> {
        val refreshToken = tokenLocalDataSource.getRefreshToken() ?: ""
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