package com.acon.acon.data.repository

import com.acon.acon.core.model.model.user.VerificationStatus
import com.acon.acon.core.model.type.SocialType
import com.acon.acon.data.cache.ProfileInfoCache
import com.acon.acon.data.session.SessionHandler
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.datasource.local.UserLocalDataSource
import com.acon.acon.data.datasource.remote.UserRemoteDataSource
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.SignInRequest
import com.acon.acon.data.dto.request.SignOutRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.user.PostSignOutError
import com.acon.acon.domain.error.user.PostSignInError
import com.acon.acon.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val sessionHandler: SessionHandler,
    private val profileInfoCache: ProfileInfoCache
) : UserRepository {

    override fun getUserType() = sessionHandler.getUserType()

    override suspend fun signIn(
        socialType: SocialType,
        idToken: String
    ): Result<VerificationStatus> {
        return runCatchingWith(PostSignInError()) {
            val signInResponse = userRemoteDataSource.signIn(
                SignInRequest(
                    socialType = socialType,
                    idToken = idToken
                )
            )

            sessionHandler.completeSignIn(
                signInResponse.accessToken.orEmpty(),
                signInResponse.refreshToken.orEmpty()
            )
            saveDidOnboarding(signInResponse.toVerificationStatus().hasPreference)

            signInResponse.toVerificationStatus()
        }
    }

    override suspend fun signOut(): Result<Unit> {
        val refreshToken = tokenLocalDataSource.getRefreshToken() ?: ""
        return runCatchingWith(PostSignOutError()) {
            userRemoteDataSource.signOut(
                SignOutRequest(refreshToken = refreshToken)
            )
        }.onSuccess {
            saveDidOnboarding(false)
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
            saveDidOnboarding(false)
            clearSession()
        }
    }

    override suspend fun saveDidOnboarding(didOnboarding: Boolean): Result<Unit> {
        return runCatchingWith {
            userLocalDataSource.saveDidOnboarding(didOnboarding)
        }
    }

    override suspend fun getDidOnboarding(): Result<Boolean> {
        return runCatchingWith {
            userLocalDataSource.getDidOnboarding()
        }
    }

    override suspend fun clearSession() = runCatchingWith {
        profileInfoCache.clearData()
        sessionHandler.clearSession()
    }
}