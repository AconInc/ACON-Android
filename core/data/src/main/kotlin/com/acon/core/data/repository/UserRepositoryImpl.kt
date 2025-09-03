package com.acon.core.data.repository

import com.acon.acon.core.model.model.user.CredentialCode
import com.acon.acon.core.model.model.user.SocialPlatform
import com.acon.acon.core.model.model.user.VerificationStatus
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.domain.error.user.PostSignInError
import com.acon.acon.domain.error.user.PostSignOutError
import com.acon.acon.domain.repository.OnboardingRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.core.data.cache.ProfileInfoCacheLegacy
import com.acon.core.data.datasource.local.TokenLocalDataSource
import com.acon.core.data.datasource.remote.UserRemoteDataSource
import com.acon.core.data.dto.request.SignInRequest
import com.acon.core.data.dto.request.SignOutRequest
import com.acon.core.data.error.runCatchingWith
import com.acon.core.data.session.SessionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val sessionHandler: SessionHandler,
    private val profileInfoCacheLegacy: ProfileInfoCacheLegacy,
    private val onboardingRepository: OnboardingRepository
) : UserRepository {

    override fun getUserType() = sessionHandler.getUserType()

    override suspend fun signIn(
        socialType: SocialPlatform,
        code: CredentialCode
    ): Result<VerificationStatus> {
        return runCatchingWith(PostSignInError()) {
            val signInResponse = userRemoteDataSource.signIn(
                SignInRequest(
                    platform = socialType,
                    idToken = code.value
                )
            )

            sessionHandler.completeSignIn(
                signInResponse.accessToken.orEmpty(),
                signInResponse.refreshToken.orEmpty()
            )

            coroutineScope {
                val verifiedAreaJob = async {
                    onboardingRepository.updateHasVerifiedArea(signInResponse.toVerificationStatus().hasVerifiedArea)
                }
                val tastePreferenceJob = async {
                    onboardingRepository.updateHasTastePreference(signInResponse.toVerificationStatus().hasPreference)
                }

                awaitAll(verifiedAreaJob, tastePreferenceJob)
            }

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
            onboardingRepository.updateHasVerifiedArea(false)
            onboardingRepository.updateHasTastePreference(false)
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
            onboardingRepository.updateHasVerifiedArea(false)
            onboardingRepository.updateHasTastePreference(false)
            clearSession()
        }
    }

    override suspend fun clearSession() = runCatchingWith {
        profileInfoCacheLegacy.clearData()
        sessionHandler.clearSession()
    }
}