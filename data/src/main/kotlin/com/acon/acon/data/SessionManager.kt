package com.acon.acon.data

import com.acon.acon.core.common.IODispatcher
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.domain.type.LocalVerificationType
import com.acon.acon.domain.type.UserType
import com.acon.acon.domain.type.VerificationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    @IODispatcher private val scope: CoroutineScope
) {
    private val _localVerification = MutableStateFlow(LocalVerificationType.NOT_VERIFIED)
    private val localVerification = flow {
        val areaVerification = tokenLocalDataSource.getAreaVerification()
        val type = if (areaVerification) LocalVerificationType.VERIFIED
        else LocalVerificationType.NOT_VERIFIED
        emit(VerificationState.Loaded(type))
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = VerificationState.Loading
    )

    private val _userType = MutableStateFlow(UserType.GUEST)
    private val userType = flow {
        val accessToken = tokenLocalDataSource.getAccessToken()
        if (accessToken.isNullOrEmpty())
            _userType.emit(UserType.GUEST)
        else
            _userType.emit(UserType.USER)

        emitAll(_userType)
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = UserType.GUEST
    )

    fun getUserType(): Flow<UserType> {
        return userType
    }

    suspend fun saveAccessToken(accessToken: String) {
        tokenLocalDataSource.saveAccessToken(accessToken)
        _userType.emit(UserType.USER)
    }

    fun getLocalVerificationType(): Flow<LocalVerificationType> {
        return localVerification
            .filterIsInstance<VerificationState.Loaded>()
            .map { it.type }
    }

    suspend fun updateLocalVerificationType(isVerified: Boolean) {
        tokenLocalDataSource.saveAreaVerification(isVerified)
        if(isVerified) { _localVerification.emit(LocalVerificationType.VERIFIED) }
        else { _localVerification.emit(LocalVerificationType.NOT_VERIFIED) }
    }

    suspend fun clearSession() {
        tokenLocalDataSource.removeAllTokens()
        _userType.emit(UserType.GUEST)
        _localVerification.emit(LocalVerificationType.NOT_VERIFIED)
    }
}