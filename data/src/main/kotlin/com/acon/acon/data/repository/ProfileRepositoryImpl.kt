package com.acon.acon.data.repository

import com.acon.acon.core.common.IODispatcher
import com.acon.acon.core.model.model.area.Area
import com.acon.acon.data.cache.ProfileInfoCache
import com.acon.acon.data.datasource.remote.ProfileRemoteDataSource
import com.acon.acon.data.dto.request.SaveSpotRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.profile.SaveSpotError
import com.acon.acon.domain.error.profile.ValidateNicknameError
import com.acon.acon.core.model.model.profile.PreSignedUrl
import com.acon.acon.core.model.model.profile.ProfileInfo
import com.acon.acon.core.model.model.profile.SavedSpot
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.core.model.type.UpdateProfileType
import com.acon.acon.domain.error.area.DeleteVerifiedAreaError
import com.acon.acon.domain.error.area.ReplaceVerifiedArea
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    @IODispatcher private val scope: CoroutineScope,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val profileInfoCache: ProfileInfoCache
) : ProfileRepository {

    override fun fetchProfile(): Flow<Result<ProfileInfo>> {
        return flow {
            emit(runCatchingWith {
                profileRemoteDataSource.fetchProfile().toProfile()
            })
        }
        return  profileInfoCache.data
    }

    override suspend fun getPreSignedUrl(): Result<PreSignedUrl> {
        return runCatchingWith() {
            profileRemoteDataSource.getPreSignedUrl().toPreSignedUrl()
        }
    }

    override suspend fun validateNickname(nickname: String): Result<Unit> {
        return runCatchingWith(ValidateNicknameError()) {
            profileRemoteDataSource.validateNickname(nickname)
        }
    }

    override suspend fun updateProfile(fileName: String, nickname: String, birthday: String?, uri: String): Result<Unit> {
        return runCatchingWith() {
            profileRemoteDataSource.updateProfile(fileName, nickname, birthday)
            profileInfoCache.updateData(
                ProfileInfo(
                    nickname = nickname,
                    birthDate = birthday,
                    image = uri,
                    savedSpots = profileInfoCache.data.value.getOrNull()?.savedSpots.orEmpty()
                )
            )
        }
    }

    private val _updateProfileType = MutableStateFlow(UpdateProfileType.IDLE)
    private val updateProfileType = flow {
        emitAll(_updateProfileType)
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = UpdateProfileType.IDLE
    )

    override fun updateProfileType(type: UpdateProfileType) {
        _updateProfileType.value = type
    }

    override fun getProfileType(): Flow<UpdateProfileType> {
        return updateProfileType
    }

    override suspend fun resetProfileType() {
        _updateProfileType.emit(UpdateProfileType.IDLE)
    }

    override suspend fun fetchSavedSpots(): Result<List<SavedSpot>> {
        return runCatchingWith() {
            profileRemoteDataSource.fetchSavedSpots().savedSpotResponseList?.map {
                it.toSavedSpot()
            }.orEmpty()
        }
    }

    override suspend fun saveSpot(spotId: Long): Result<Unit> {
        return runCatchingWith(SaveSpotError()) {
            profileRemoteDataSource.saveSpot(SaveSpotRequest(spotId))
        }
    }

    override suspend fun verifyArea(
        latitude: Double,
        longitude: Double
    ): Result<Unit> = runCatchingWith() {
        // TODO - 동네인증 API Error 처리 안됨
        profileRemoteDataSource.verifyArea(
            latitude = latitude,
            longitude = longitude
        )
    }

    override suspend fun fetchVerifiedAreaList(): Result<List<Area>> {
        // TODO - 인증 지역 조회 API Error 처리 안됨
        return runCatchingWith() {
            profileRemoteDataSource.fetchVerifiedAreaList().verifiedAreaList
                .map { it.toVerifiedArea() }
        }
    }

    override suspend fun replaceVerifiedArea(
        previousVerifiedAreaId: Long,
        latitude: Double,
        longitude: Double
    ): Result<Unit> {
        return runCatchingWith(ReplaceVerifiedArea()) {
            profileRemoteDataSource.replaceVerifiedArea(
                previousVerifiedAreaId = previousVerifiedAreaId,
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    override suspend fun deleteVerifiedArea(verifiedAreaId: Long): Result<Unit> {
        return runCatchingWith(DeleteVerifiedAreaError()) {
            profileRemoteDataSource.deleteVerifiedArea(verifiedAreaId)
        }
    }
}