package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.LoginRequest
import com.acon.acon.data.dto.response.LoginResponse
import com.acon.acon.data.remote.UserApi
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApi: UserApi
) {

    suspend fun postLogin(googleLoginRequest: LoginRequest): LoginResponse {
        return userApi.postLogin(googleLoginRequest)
    }
}