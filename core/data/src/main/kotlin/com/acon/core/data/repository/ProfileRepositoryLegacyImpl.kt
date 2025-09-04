package com.acon.core.data.repository

import com.acon.acon.core.common.IODispatcher
import com.acon.acon.core.model.model.area.Area
import com.acon.core.data.cache.ProfileInfoCacheLegacy
import com.acon.core.data.datasource.remote.ProfileRemoteDataSourceLegacy
import com.acon.core.data.dto.request.SaveSpotRequest
import com.acon.core.data.error.runCatchingWith
import com.acon.acon.domain.error.profile.SaveSpotError
import com.acon.acon.domain.error.profile.ValidateNicknameErrorLegacy
import com.acon.acon.core.model.model.profile.PreSignedUrl
import com.acon.acon.core.model.model.profile.ProfileInfoLegacy
import com.acon.acon.core.model.model.profile.SavedSpotLegacy
import com.acon.acon.domain.repository.ProfileRepositoryLegacy
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
import javax.inject.Inject

class ProfileRepositoryLegacyImpl @Inject constructor(
    @IODispatcher private val scope: CoroutineScope,
    private val profileRemoteDataSourceLegacy: ProfileRemoteDataSourceLegacy,
    private val profileInfoCacheLegacy: ProfileInfoCacheLegacy
) : ProfileRepositoryLegacy {

    override fun fetchProfile(): Flow<Result<ProfileInfoLegacy>> {
        return flow {
            emit(runCatchingWith {
                profileRemoteDataSourceLegacy.fetchProfile().toProfile()
            })
        }
        return  profileInfoCacheLegacy.data
    }

    override suspend fun getPreSignedUrl(): Result<PreSignedUrl> {
        return runCatchingWith() {
            profileRemoteDataSourceLegacy.getPreSignedUrl().toPreSignedUrl()
        }
    }

    override suspend fun validateNickname(nickname: String): Result<Unit> {
        return runCatchingWith(ValidateNicknameErrorLegacy()) {
            profileRemoteDataSourceLegacy.validateNickname(nickname)
        }
    }

    override suspend fun updateProfile(fileName: String, nickname: String, birthday: String?, uri: String): Result<Unit> {
        return runCatchingWith() {
            profileRemoteDataSourceLegacy.updateProfile(fileName, nickname, birthday)
            profileInfoCacheLegacy.updateData(
                ProfileInfoLegacy(
                    nickname = nickname,
                    birthDate = birthday,
                    image = uri,
                    savedSpotLegacies = profileInfoCacheLegacy.data.value.getOrNull()?.savedSpotLegacies.orEmpty()
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

    override suspend fun fetchSavedSpots(): Result<List<SavedSpotLegacy>> {
        return runCatchingWith() {
            profileRemoteDataSourceLegacy.fetchSavedSpots().savedSpotResponseLegacyList?.map {
                it.toSavedSpot()
            }.orEmpty()
        }
    }

    override suspend fun saveSpot(spotId: Long): Result<Unit> {
        return runCatchingWith(SaveSpotError()) {
            profileRemoteDataSourceLegacy.saveSpot(SaveSpotRequest(spotId))
        }
    }

    override suspend fun fetchVerifiedAreaList(): Result<List<Area>> {
        // TODO - 인증 지역 조회 API Error 처리 안됨
        return runCatchingWith() {
            profileRemoteDataSourceLegacy.fetchVerifiedAreaList().verifiedAreaList
                .map { it.toVerifiedArea() }
        }
    }

    override suspend fun replaceVerifiedArea(
        previousVerifiedAreaId: Long,
        latitude: Double,
        longitude: Double
    ): Result<Unit> {
        return runCatchingWith(ReplaceVerifiedArea()) {
            profileRemoteDataSourceLegacy.replaceVerifiedArea(
                previousVerifiedAreaId = previousVerifiedAreaId,
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    override suspend fun deleteVerifiedArea(verifiedAreaId: Long): Result<Unit> {
        return runCatchingWith(DeleteVerifiedAreaError()) {
            profileRemoteDataSourceLegacy.deleteVerifiedArea(verifiedAreaId)
        }
    }
}