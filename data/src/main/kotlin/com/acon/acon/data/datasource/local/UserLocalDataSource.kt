package com.acon.acon.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.acon.acon.data.di.UserDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    @UserDataStore private val userDataStore: DataStore<Preferences>
) {

    suspend fun saveDidOnboarding(didOnboarding: Boolean) {
        userDataStore.edit {
            it[DID_ONBOARDING] = didOnboarding
        }
    }

    suspend fun getDidOnboarding(): Boolean {
        return userDataStore.data.map { preferences ->
            preferences[DID_ONBOARDING] ?: false
        }.first()
    }

    companion object {
        private val DID_ONBOARDING = booleanPreferencesKey("did_onboarding")
    }
}