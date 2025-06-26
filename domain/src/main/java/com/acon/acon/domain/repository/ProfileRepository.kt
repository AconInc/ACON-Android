package com.acon.acon.domain.repository

import com.acon.core.model.profile.PreSignedUrl
import com.acon.core.model.profile.ProfileInfo
import com.acon.core.model.profile.SavedSpot
import com.acon.core.type.UpdateProfileType
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun fetchProfile(): Flow<Result<ProfileInfo>>

    suspend fun getPreSignedUrl(): Result<PreSignedUrl>

    suspend fun validateNickname(nickname: String): Result<Unit>

    suspend fun updateProfile(fileName: String, nickname: String, birthday: String?, uri: String): Result<Unit>

    fun getProfileType(): Flow<UpdateProfileType>

    fun updateProfileType(type: UpdateProfileType)

    suspend fun resetProfileType()

    suspend fun fetchSavedSpots(): Result<List<SavedSpot>>

    suspend fun saveSpot(spotId: Long): Result<Unit>
}