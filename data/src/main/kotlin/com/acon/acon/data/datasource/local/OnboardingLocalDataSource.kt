package com.acon.acon.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.acon.acon.data.di.OnboardingDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OnboardingLocalDataSource @Inject constructor(
    @OnboardingDataStore private val onboardingDataStore: DataStore<Preferences>
) {

    suspend fun saveDidOnboarding(didOnboarding: Boolean) {
        onboardingDataStore.edit { prefs ->
            prefs[DID_ONBOARDING] = didOnboarding
        }
    }

    suspend fun getDidOnboarding(): Boolean {
        return onboardingDataStore.data.map { prefs ->
            prefs[DID_ONBOARDING] ?: false
        }.first()
    }

    companion object {
        private val DID_ONBOARDING = booleanPreferencesKey("did_onboarding")
    }
}