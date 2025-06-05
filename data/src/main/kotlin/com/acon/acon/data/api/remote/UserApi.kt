package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.LoginRequest
import com.acon.acon.data.dto.request.LogoutRequest
import com.acon.acon.data.dto.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/api/v1/auth/login")
    suspend fun postLogin(
        @Body body: LoginRequest
    ) : LoginResponse

    @POST("/api/v1/auth/logout")
    suspend fun postLogout(
        @Body body: LogoutRequest
    )

    @POST("/api/v1/members/withdrawal")
    suspend fun postDeleteAccount(
        @Body body: DeleteAccountRequest
    )
}