package com.acon.acon.data

import com.acon.acon.core.common.IODispatcher
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.core.model.type.UserType
import com.acon.core.analytics.amplitude.AconAmplitude
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    @IODispatcher private val scope: CoroutineScope
) {

    private val _userType = MutableStateFlow(com.acon.acon.core.model.type.UserType.GUEST)
    private val userType = flow {
        val accessToken = tokenLocalDataSource.getAccessToken()
        if (accessToken.isNullOrEmpty())
            _userType.emit(com.acon.acon.core.model.type.UserType.GUEST)
        else
            _userType.emit(com.acon.acon.core.model.type.UserType.USER)

        emitAll(_userType)
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = com.acon.acon.core.model.type.UserType.GUEST
    )

    fun getUserType(): Flow<com.acon.acon.core.model.type.UserType> {
        return userType
    }

    suspend fun saveAccessToken(accessToken: String) {
        tokenLocalDataSource.saveAccessToken(accessToken)
        _userType.emit(com.acon.acon.core.model.type.UserType.USER)
    }

    suspend fun clearSession() {
        AconAmplitude.clearUserId()
        tokenLocalDataSource.removeAllTokens()
        _userType.emit(com.acon.acon.core.model.type.UserType.GUEST)
    }
}