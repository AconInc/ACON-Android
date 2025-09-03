package com.acon.core.data.api.remote

import com.acon.core.data.dto.request.profile.UpdateProfileRequest
import com.acon.core.data.dto.response.profile.ProfileResponse
import com.acon.core.data.dto.response.profile.SavedSpotResponse

interface ProfileApi {

    suspend fun getProfile() : ProfileResponse
    suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest)
    suspend fun validateNickname(nickname: String)
    suspend fun getSavedSpots() : List<SavedSpotResponse>
}