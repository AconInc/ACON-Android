package com.acon.core.data.datasource.local

import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import com.acon.acon.core.common.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenLocalDataSource @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val preferences: EncryptedSharedPreferences
) {

    internal suspend fun saveAccessToken(
        accessToken: String,
    ) = withContext(dispatcher) {
        preferences.edit {
            putString(SHARED_PREF_KEY, accessToken)
        }
    }

    internal suspend fun saveRefreshToken(
        refreshToken: String,
    ) = withContext(dispatcher) {
        preferences.edit {
            putString(SHARED_PREF_REFRESH_KEY, refreshToken)
        }
    }

    internal suspend fun getAccessToken(): String? = withContext(dispatcher) {
        preferences.getString(SHARED_PREF_KEY, null)
    }

    internal suspend fun getRefreshToken(): String? = withContext(dispatcher) {
        preferences.getString(SHARED_PREF_REFRESH_KEY, null)
    }

    internal suspend fun removeAllTokens() = withContext(dispatcher) {
        preferences.edit {
            remove(SHARED_PREF_KEY)
            remove(SHARED_PREF_REFRESH_KEY)
        }
    }

    companion object {
        private const val SHARED_PREF_KEY = "access_token"
        private const val SHARED_PREF_REFRESH_KEY = "refresh_token"
    }
}