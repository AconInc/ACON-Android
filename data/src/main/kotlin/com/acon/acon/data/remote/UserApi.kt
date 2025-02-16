package com.acon.acon.data.remote

import com.acon.acon.data.dto.request.LoginRequest
import com.acon.acon.data.dto.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/api/v1/auth/login")
    suspend fun postLogin(
        @Body body: LoginRequest
    ) : LoginResponse
}