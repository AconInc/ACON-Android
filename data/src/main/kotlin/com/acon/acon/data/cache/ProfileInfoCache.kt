package com.acon.acon.data.cache

import com.acon.acon.data.cache.base.ReadWriteCache
import com.acon.acon.data.datasource.remote.ProfileRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ProfileInfoCache @Inject constructor(
    private val scope: CoroutineScope,
    private val profileRemoteDataSource: ProfileRemoteDataSource
) : ReadWriteCache<com.acon.core.model.profile.ProfileInfo>(scope) {

    override val emptyData = Result.success(com.acon.core.model.profile.ProfileInfo.Empty)

    override suspend fun fetchRemoteData(): com.acon.core.model.profile.ProfileInfo {
        return profileRemoteDataSource.fetchProfile().toProfile()
    }
}
