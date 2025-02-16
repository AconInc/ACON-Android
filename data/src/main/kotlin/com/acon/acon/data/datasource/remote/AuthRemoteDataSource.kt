package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.GoogleTokenRequest
import com.acon.acon.data.dto.response.GoogleTokenResponse
import com.acon.acon.data.remote.AuthApi
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi
) {

    suspend fun postLogin(googleLoginRequest: GoogleTokenRequest): GoogleTokenResponse {
        return authApi.postLogin(googleLoginRequest)
    }
}