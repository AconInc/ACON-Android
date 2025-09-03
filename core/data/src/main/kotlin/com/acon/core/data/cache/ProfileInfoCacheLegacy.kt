package com.acon.core.data.cache

import com.acon.acon.core.model.model.profile.ProfileInfoLegacy
import com.acon.core.data.cache.base.ReadWriteCache
import com.acon.core.data.datasource.remote.ProfileRemoteDataSourceLegacy
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ProfileInfoCacheLegacy @Inject constructor(
    private val scope: CoroutineScope,
    private val profileRemoteDataSourceLegacy: ProfileRemoteDataSourceLegacy
) : ReadWriteCache<ProfileInfoLegacy>(scope) {

    override val emptyData = Result.success(ProfileInfoLegacy.Empty)

    override suspend fun fetchRemoteData(): ProfileInfoLegacy {
        return profileRemoteDataSourceLegacy.fetchProfile().toProfile()
    }
}
