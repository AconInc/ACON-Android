package com.acon.core.data.repository

import com.acon.acon.core.model.model.profile.Profile
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.core.data.datasource.local.ProfileLocalDataSource
import com.acon.core.data.datasource.remote.ProfileRemoteDataSource
import com.acon.core.data.error.runCatchingWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val profileLocalDataSource: ProfileLocalDataSource
) : ProfileRepository {

    override fun getProfile(): Flow<Result<Profile>> = flow {
        if (profileLocalDataSource.isCachedProfileExist()) {
            try {
                emit(Result.success(profileLocalDataSource.getProfile()))
            } catch (_: Exception) {
                emit(getProfileFromRemote())
            }
        } else {
            emit(getProfileFromRemote())
        }
    }

    private suspend fun getProfileFromRemote(): Result<Profile> {
        return runCatchingWith {
            val profileResponse = profileRemoteDataSource.getProfile()
            val profile = profileResponse.toProfile()

            profileLocalDataSource.cacheProfile(profile)

            profile
        }
    }
}