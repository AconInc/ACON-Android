package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.response.profile.PreSignedUrlResponse
import com.acon.acon.data.remote.ProfileApi
import com.acon.acon.data.dto.response.profile.ProfileResponse
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
}