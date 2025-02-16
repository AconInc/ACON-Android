package com.acon.data.datasource.remote

import com.acon.data.api.remote.ProfileApi
import com.acon.data.dto.response.profile.ProfileResponse
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(
    private val profileApi: ProfileApi
) {
    suspend fun fetchProfile(): ProfileResponse {
        return profileApi.fetchProfile()
    }
}