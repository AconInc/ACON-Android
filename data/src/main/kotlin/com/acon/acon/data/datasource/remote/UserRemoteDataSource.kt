package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.GoogleTokenRequest
import com.acon.acon.data.dto.response.GoogleTokenResponse
import com.acon.acon.data.remote.UserApi
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApi: UserApi
) {

    suspend fun postLogin(googleLoginRequest: GoogleTokenRequest): GoogleTokenResponse {
        return userApi.postLogin(googleLoginRequest)
    }
}