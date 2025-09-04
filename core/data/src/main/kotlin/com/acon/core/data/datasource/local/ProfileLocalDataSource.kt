package com.acon.core.data.datasource.local

import com.acon.acon.core.model.model.profile.Profile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProfileLocalDataSource {

    suspend fun cacheProfile(profile: Profile)
    fun getProfile() : Flow<Profile?>
}
//
//class ProfileLocalDataSourceImpl @Inject constructor(
//
//) : ProfileLocalDataSource {
//
//    override suspend fun cacheProfile(profile: Profile) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getProfile(): Profile {
//        TODO("Not yet implemented")
//    }
//}