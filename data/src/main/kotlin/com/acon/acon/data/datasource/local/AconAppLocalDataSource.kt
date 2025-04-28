package com.acon.acon.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AconAppLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun setUpdatePostponeTime(time: Long) {
        dataStore.edit {
            it[UPDATE_POSTPONE_TIME] = time
        }
    }

    suspend fun getUpdatePostponeTime(): Long {
        return dataStore.data.map { preferences ->
            preferences[UPDATE_POSTPONE_TIME] ?: 0L
        }.first()
    }

    companion object {
        private val UPDATE_POSTPONE_TIME = longPreferencesKey("update_postpone_time")
    }
}