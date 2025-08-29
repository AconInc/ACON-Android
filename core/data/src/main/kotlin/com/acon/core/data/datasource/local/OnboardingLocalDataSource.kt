package com.acon.core.data.datasource.local

import androidx.datastore.core.DataStore
import com.acon.core.data.dto.entity.OnboardingPreferencesEntity
import com.acon.core.data.dto.entity.copy
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OnboardingLocalDataSource @Inject constructor(
    private val onboardingDataStore: DataStore<OnboardingPreferencesEntity>
) {

    suspend fun updateOnboardingPreferences(pref: OnboardingPreferencesEntity) {
        onboardingDataStore.updateData { prefs ->
            prefs.copy {
                shouldShowIntroduce = pref.shouldShowIntroduce
                hasTastePreference = pref.hasTastePreference
                hasVerifiedArea = pref.hasVerifiedArea
            }
        }
    }

    suspend fun updateShouldShowIntroduce(shouldShow: Boolean) {
        onboardingDataStore.updateData { prefs ->
            prefs.copy {
                shouldShowIntroduce = shouldShow
            }
        }
    }

    suspend fun updateHasPreference(hasPreference: Boolean) {
        onboardingDataStore.updateData { prefs ->
            prefs.copy {
                this.hasTastePreference = hasPreference
            }
        }
    }

    suspend fun updateHasVerifiedArea(verifiedArea: Boolean) {
        onboardingDataStore.updateData { prefs ->
            prefs.copy {
                this.hasVerifiedArea = verifiedArea
            }
        }
    }

    suspend fun getOnboardingPreferences(): OnboardingPreferencesEntity {
        return onboardingDataStore.data.first()
    }
}