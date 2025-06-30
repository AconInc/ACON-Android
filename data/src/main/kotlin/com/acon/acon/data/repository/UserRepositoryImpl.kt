package com.acon.acon.data.repository

import com.acon.acon.core.model.model.user.VerificationStatus
import com.acon.acon.core.model.type.SocialType
import com.acon.acon.core.model.type.UserType
import com.acon.acon.data.SessionHandler
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.datasource.remote.UserRemoteDataSource
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.SignInRequest
import com.acon.acon.data.dto.request.SignOutRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.user.PostSignOutError
import com.acon.acon.domain.error.user.PostSignInError
import com.acon.acon.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val sessionHandler: SessionHandler,
) : UserRepository {

    override fun getUserType() = sessionHandler.getUserType()

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

            sessionHandler.completeSignIn(signInResponse.accessToken.orEmpty(), signInResponse.refreshToken.orEmpty())

            signInResponse.toVerificationStatus()
        }
    }

    override suspend fun signOut(): Result<Unit> {
        val refreshToken = tokenLocalDataSource.getRefreshToken() ?: ""
        return runCatchingWith(*PostSignOutError.createErrorInstances()) {
            userRemoteDataSource.signOut(
                SignOutRequest(refreshToken = refreshToken)
            )
        }.onSuccess {
            clearSession()
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
            clearSession()
        }
    }

    override suspend fun clearSession() = runCatchingWith {
        sessionHandler.clearSession()
    }
}