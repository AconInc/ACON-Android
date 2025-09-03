package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.area.Area
import com.acon.acon.core.model.model.profile.PreSignedUrl
import com.acon.acon.core.model.model.profile.ProfileInfoLegacy
import com.acon.acon.core.model.model.profile.SavedSpotLegacy
import com.acon.acon.core.model.type.UpdateProfileType
import kotlinx.coroutines.flow.Flow

interface ProfileRepositoryLegacy {
    fun fetchProfile(): Flow<Result<ProfileInfoLegacy>>

    suspend fun getPreSignedUrl(): Result<PreSignedUrl>

    suspend fun validateNickname(nickname: String): Result<Unit>

    suspend fun updateProfile(fileName: String, nickname: String, birthday: String?, uri: String): Result<Unit>

    fun getProfileType(): Flow<UpdateProfileType>

    fun updateProfileType(type: UpdateProfileType)

    suspend fun resetProfileType()

    suspend fun fetchSavedSpots(): Result<List<SavedSpotLegacy>>

    suspend fun saveSpot(spotId: Long): Result<Unit>

    suspend fun fetchVerifiedAreaList(): Result<List<Area>>

    suspend fun replaceVerifiedArea(
        previousVerifiedAreaId: Long,
        latitude: Double,
        longitude: Double
    ): Result<Unit>

    suspend fun deleteVerifiedArea(verifiedAreaId: Long): Result<Unit>
}