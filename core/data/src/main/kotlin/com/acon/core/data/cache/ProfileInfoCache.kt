package com.acon.core.data.cache

import com.acon.acon.core.model.model.profile.ProfileInfo
import com.acon.core.data.cache.base.ReadWriteCache
import com.acon.core.data.datasource.remote.ProfileRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ProfileInfoCache @Inject constructor(
    private val scope: CoroutineScope,
    private val profileRemoteDataSource: ProfileRemoteDataSource
) : ReadWriteCache<ProfileInfo>(scope) {

    override val emptyData = Result.success(ProfileInfo.Empty)

    override suspend fun fetchRemoteData(): ProfileInfo {
        return profileRemoteDataSource.fetchProfile().toProfile()
    }
}
