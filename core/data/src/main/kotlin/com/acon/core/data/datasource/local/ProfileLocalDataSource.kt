package com.acon.core.data.datasource.local

import com.acon.acon.core.model.model.profile.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ProfileLocalDataSource {

    suspend fun cacheProfile(profile: Profile)
    fun getProfile() : Flow<Profile?>
    suspend fun clearCache()
}

class ProfileLocalDataSourceImpl constructor(

) : ProfileLocalDataSource {

    private val _profile = MutableStateFlow<Profile?>(null)
    private val profile = _profile.asStateFlow()

    override suspend fun cacheProfile(profile: Profile) {
        _profile.value = profile
    }

    override fun getProfile(): Flow<Profile?> {
        return profile
    }

    override suspend fun clearCache() {
        _profile.value = null
    }
}