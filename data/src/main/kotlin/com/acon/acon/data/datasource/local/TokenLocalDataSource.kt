package com.acon.acon.data.datasource.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.acon.acon.core.common.IODispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenLocalDataSource @Inject constructor(
    @ApplicationContext applicationContext: Context,
    @IODispatcher private val dispatchersIO: CoroutineDispatcher,
) {
    private var masterKey =
        MasterKey.Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    private var sharedPreferences = EncryptedSharedPreferences.create(
        applicationContext,
        SHARED_PREF_FILENAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun saveAccessToken(
        accessToken: String,
    ) = withContext(dispatchersIO) {
        with(sharedPreferences.edit()) {
            putString(SHARED_PREF_KEY, accessToken)
            apply()
        }
    }

    suspend fun saveRefreshToken(
        refreshToken: String,
    ) = withContext(dispatchersIO) {
        with(sharedPreferences.edit()) {
            putString(SHARED_PREF_REFRESH_KEY, refreshToken)
            apply()
        }
    }

    suspend fun getAccessToken(): String? = withContext(dispatchersIO) {
        sharedPreferences.getString(SHARED_PREF_KEY, null)
    }

    suspend fun getRefreshToken(): String? = withContext(dispatchersIO) {
        sharedPreferences.getString(SHARED_PREF_REFRESH_KEY, null)
    }

    suspend fun removeAccessToken() = withContext(dispatchersIO) {
        with(sharedPreferences.edit()) {
            remove(SHARED_PREF_KEY)
            apply()
        }
    }

    suspend fun removeRefreshToken() = withContext(dispatchersIO) {
        with(sharedPreferences.edit()) {
            remove(SHARED_PREF_REFRESH_KEY)
            apply()
        }
    }

    suspend fun removeAllTokens() = withContext(dispatchersIO) {
        with(sharedPreferences.edit()) {
            remove(SHARED_PREF_KEY)
            remove(SHARED_PREF_REFRESH_KEY)
            apply()
        }
    }

    companion object {
        private const val SHARED_PREF_FILENAME = "token"
        private const val SHARED_PREF_KEY = "accessToken"
        private const val SHARED_PREF_REFRESH_KEY = "refreshToken"
    }
}