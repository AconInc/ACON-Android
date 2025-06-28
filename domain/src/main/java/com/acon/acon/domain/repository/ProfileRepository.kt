package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.profile.PreSignedUrl
import com.acon.acon.core.model.model.profile.ProfileInfo
import com.acon.acon.core.model.model.profile.SavedSpot
import com.acon.acon.core.model.type.UpdateProfileType
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun fetchProfile(): Flow<Result<com.acon.acon.core.model.model.profile.ProfileInfo>>

    suspend fun getPreSignedUrl(): Result<com.acon.acon.core.model.model.profile.PreSignedUrl>

    suspend fun validateNickname(nickname: String): Result<Unit>

    suspend fun updateProfile(fileName: String, nickname: String, birthday: String?, uri: String): Result<Unit>

    fun getProfileType(): Flow<com.acon.acon.core.model.type.UpdateProfileType>

    fun updateProfileType(type: com.acon.acon.core.model.type.UpdateProfileType)

    suspend fun resetProfileType()

    suspend fun fetchSavedSpots(): Result<List<com.acon.acon.core.model.model.profile.SavedSpot>>

    suspend fun saveSpot(spotId: Long): Result<Unit>
}