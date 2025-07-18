package com.acon.acon.data.cache

import com.acon.acon.data.cache.base.ReadWriteCache
import com.acon.acon.data.datasource.remote.ProfileRemoteDataSource
import com.acon.acon.domain.model.profile.ProfileInfo
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
