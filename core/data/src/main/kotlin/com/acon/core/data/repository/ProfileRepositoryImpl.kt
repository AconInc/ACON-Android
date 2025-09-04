package com.acon.core.data.repository

import com.acon.acon.core.model.model.profile.Profile
import com.acon.acon.core.model.model.profile.SavedSpot
import com.acon.acon.domain.error.profile.UpdateProfileError
import com.acon.acon.domain.error.profile.ValidateNicknameError
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.core.data.datasource.local.ProfileLocalDataSource
import com.acon.core.data.datasource.remote.ProfileRemoteDataSource
import com.acon.core.data.dto.request.profile.toUpdateProfileRequest
import com.acon.core.data.error.runCatchingWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val profileLocalDataSource: ProfileLocalDataSource
) : ProfileRepository {

    override fun getProfile(): Flow<Result<Profile>> {
        return profileLocalDataSource.getProfile().flatMapLatest { cachedProfile: Profile? ->
            if (cachedProfile == null) {
                getProfileFromRemote()
            } else {
                flowOf(Result.success(cachedProfile))
            }
        }
    }

    private fun getProfileFromRemote(): Flow<Result<Profile>> {
        return flow {
            emit(runCatchingWith {
                val profileResponse = profileRemoteDataSource.getProfile()
                val profile = profileResponse.toProfile()

                profileLocalDataSource.cacheProfile(profile)

                profile
            })
        }
    }

    override suspend fun updateProfile(newProfile: Profile): Result<Unit> {
        return runCatchingWith(UpdateProfileError()) {
            profileRemoteDataSource.updateProfile(newProfile.toUpdateProfileRequest())

            profileLocalDataSource.cacheProfile(newProfile)

            Unit
        }
    }

    override suspend fun validateNickname(nickname: String) : Result<Unit> {
        return runCatchingWith(ValidateNicknameError()) {
            profileRemoteDataSource.validateNickname(nickname)
        }
    }

    override suspend fun getSavedSpots(): Result<List<SavedSpot>> {
        return runCatchingWith {
            profileRemoteDataSource.getSavedSpots().map { it.toSavedSpot() }
        }
    }
}