package com.acon.core.data.session

import com.acon.acon.core.analytics.amplitude.AconAmplitude
import com.acon.acon.core.common.IODispatcher
import com.acon.acon.core.model.type.UserType
import com.acon.core.data.datasource.local.TokenLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SessionHandler {
    suspend fun clearSession()
    suspend fun completeSignIn(accessToken: String, refreshToken: String)
    fun getUserType(): Flow<UserType>
}

class SessionHandlerImpl @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    @IODispatcher scope: CoroutineScope
) : SessionHandler {

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

    override suspend fun clearSession() {
        tokenLocalDataSource.removeAllTokens()
        _userType.value = UserType.GUEST
        AconAmplitude.clearUserId()
    }

    override suspend fun completeSignIn(accessToken: String, refreshToken: String) {
        _userType.value = UserType.USER
        tokenLocalDataSource.saveAccessToken(accessToken)
        tokenLocalDataSource.saveRefreshToken(refreshToken)
    }
}