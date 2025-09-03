package com.acon.core.data.datasource.local

import com.acon.acon.core.model.model.profile.Profile
import javax.inject.Inject

interface ProfileLocalDataSource {

    suspend fun cacheProfile(profile: Profile)
    suspend fun getProfile() : Profile
    suspend fun isCachedProfileExist() : Boolean
}

class ProfileLocalDataSourceImpl @Inject constructor(

) : ProfileLocalDataSource {

    override suspend fun cacheProfile(profile: Profile) {
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(): Profile {
        TODO("Not yet implemented")
    }

    override suspend fun isCachedProfileExist(): Boolean {
        TODO("Not yet implemented")
    }
}