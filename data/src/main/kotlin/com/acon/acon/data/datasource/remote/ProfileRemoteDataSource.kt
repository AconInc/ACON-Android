package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.AreaVerificationRequest
import com.acon.acon.data.dto.request.updateProfileRequest
import com.acon.acon.data.dto.response.AreaVerificationResponse
import com.acon.acon.data.dto.response.profile.PreSignedUrlResponse
import com.acon.acon.data.remote.ProfileApi
import com.acon.acon.data.dto.response.profile.ProfileResponse
import retrofit2.Response
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(
    private val profileApi: ProfileApi
) {
    suspend fun fetchProfile(): ProfileResponse {
        return profileApi.fetchProfile()
    }

    suspend fun getPreSignedUrl(): PreSignedUrlResponse {
        return profileApi.getPreSignedUrl()
    }

    suspend fun validateNickname(nickname: String): Response<Unit> {
        return profileApi.validateNickname(nickname)
    }

    suspend fun updateProfile(fileName: String, nickname: String, birthday: String?): Response<Unit> {
        return profileApi.updateProfile(
            request = updateProfileRequest(profileImage = fileName, nickname = nickname, birthDate = birthday)
        )
    }
}