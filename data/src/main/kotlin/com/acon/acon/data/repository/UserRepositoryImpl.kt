package com.acon.acon.data.repository

import com.acon.acon.core.analytics.amplitude.AconAmplitude
import com.acon.acon.core.common.IODispatcher
import com.acon.acon.core.model.model.user.VerificationStatus
import com.acon.acon.core.model.type.SocialType
import com.acon.acon.core.model.type.UserType
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.datasource.remote.UserRemoteDataSource
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.SignInRequest
import com.acon.acon.data.dto.request.SignOutRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.user.PostLogoutError
import com.acon.acon.domain.error.user.PostSignInError
import com.acon.acon.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    @IODispatcher scope: CoroutineScope
) : UserRepository {

    private val _userType = MutableStateFlow(UserType.GUEST)
    private val userType = _userType.asStateFlow()

    init {
        scope.launch {
            val accessToken = tokenLocalDataSource.getAccessToken()
            if (accessToken.isNullOrEmpty())
                _userType.emit(UserType.GUEST)
            else
                _userType.emit(UserType.USER)
        }
    }

    override fun getUserType(): Flow<UserType> {
        return userType
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

            _userType.value = UserType.USER

            tokenLocalDataSource.saveAccessToken(signInResponse.accessToken.orEmpty())
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
        tokenLocalDataSource.removeAllTokens()
        _userType.value = UserType.GUEST
        AconAmplitude.clearUserId()
    }
}