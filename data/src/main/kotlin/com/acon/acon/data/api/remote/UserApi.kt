package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.request.AreaVerificationRequest
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.LoginRequest
import com.acon.acon.data.dto.request.LogoutRequest
import com.acon.acon.data.dto.response.LoginResponse
import com.acon.acon.data.dto.response.area.AreaVerificationResponse
import com.acon.acon.data.dto.response.area.VerifiedAreaListResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("/api/v1/verified-areas")
    suspend fun verifyArea(
        @Body request: AreaVerificationRequest
    ): AreaVerificationResponse

    @GET("/api/v1/verified-areas")
    suspend fun fetchVerifiedAreaList() : VerifiedAreaListResponse

    @DELETE("/api/v1/verified-areas/{verifiedAreaId}")
    suspend fun deleteVerifiedArea(
        @Path("verifiedAreaId") verifiedAreaId: Long
    )
}