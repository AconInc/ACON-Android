package com.acon.acon.data.repository

import com.acon.acon.core.common.IODispatcher
import com.acon.acon.data.cache.ProfileInfoCache
import com.acon.acon.data.datasource.remote.ProfileRemoteDataSource
import com.acon.acon.data.dto.request.SaveSpotRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.profile.SaveSpotError
import com.acon.acon.domain.error.profile.ValidateNicknameError
import com.acon.core.model.profile.PreSignedUrl
import com.acon.core.model.profile.ProfileInfo
import com.acon.core.model.profile.SavedSpot
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.core.type.UpdateProfileType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    @IODispatcher private val scope: CoroutineScope,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val profileInfoCache: ProfileInfoCache
) : ProfileRepository {

    override fun fetchProfile(): Flow<Result<com.acon.core.model.profile.ProfileInfo>> {
        return profileInfoCache.data
    }

    override suspend fun getPreSignedUrl(): Result<com.acon.core.model.profile.PreSignedUrl> {
        return runCatchingWith() {
            profileRemoteDataSource.getPreSignedUrl().toPreSignedUrl()
        }
    }

    override suspend fun validateNickname(nickname: String): Result<Unit> {
        return runCatchingWith(*ValidateNicknameError.createErrorInstances()) {
            profileRemoteDataSource.validateNickname(nickname)
        }
    }

    override suspend fun updateProfile(fileName: String, nickname: String, birthday: String?, uri: String): Result<Unit> {
        return runCatchingWith() {
            profileRemoteDataSource.updateProfile(fileName, nickname, birthday)
            profileInfoCache.updateData(
                com.acon.core.model.profile.ProfileInfo(
                    nickname = nickname,
                    birthDate = birthday,
                    image = uri,
                    savedSpots = profileInfoCache.data.value.getOrNull()?.savedSpots.orEmpty()
                )
            )
        }
    }

    private val _updateProfileType = MutableStateFlow(com.acon.core.type.UpdateProfileType.IDLE)
    private val updateProfileType = flow {
        emitAll(_updateProfileType)
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = com.acon.core.type.UpdateProfileType.IDLE
    )

    override fun updateProfileType(type: com.acon.core.type.UpdateProfileType) {
        _updateProfileType.value = type
    }

    override fun getProfileType(): Flow<com.acon.core.type.UpdateProfileType> {
        return updateProfileType
    }

    override suspend fun resetProfileType() {
        _updateProfileType.emit(com.acon.core.type.UpdateProfileType.IDLE)
    }

    override suspend fun fetchSavedSpots(): Result<List<com.acon.core.model.profile.SavedSpot>> {
        return runCatchingWith() {
            profileRemoteDataSource.fetchSavedSpots().savedSpotResponseList?.map {
                it.toSavedSpot()
            }.orEmpty()
        }
    }

    override suspend fun saveSpot(spotId: Long): Result<Unit> {
        return runCatchingWith(*SaveSpotError.createErrorInstances()) {
            profileRemoteDataSource.saveSpot(SaveSpotRequest(spotId))
        }
    }
}