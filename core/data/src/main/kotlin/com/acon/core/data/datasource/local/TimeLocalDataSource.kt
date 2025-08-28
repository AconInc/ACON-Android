package com.acon.core.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.acon.acon.core.model.type.UserActionType
import com.acon.core.data.di.TimeDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimeLocalDataSource @Inject constructor(
    @TimeDataStore private val dataStore: DataStore<Preferences>
) {

    suspend fun saveUserActionTime(action: UserActionType, timestamp: Long) {
        dataStore.edit {
            it[getTimestampPreferenceKey(action)] = timestamp
        }
    }

    suspend fun getUserActionTime(action: UserActionType) : Long? {
        return dataStore.data.map {
            it[getTimestampPreferenceKey(action)] ?: -1L
        }.first().takeIf { it != -1L }
    }

    private fun getTimestampPreferenceKey(action: UserActionType) = longPreferencesKey(action.name)
}