package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.request.AreaVerificationRequest
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.ReplaceVerifiedAreaRequest
import com.acon.acon.data.dto.request.SignInRequest
import com.acon.acon.data.dto.request.SignOutRequest
import com.acon.acon.data.dto.response.SignInResponse
import com.acon.acon.data.dto.response.area.VerifiedAreaListResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @POST("/api/v1/auth/login")
    suspend fun postSignIn(
        @Body body: SignInRequest
    ) : SignInResponse

    @POST("/api/v1/auth/logout")
    suspend fun postLogout(
        @Body body: SignOutRequest
    )

    @POST("/api/v1/members/withdrawal")
    suspend fun postDeleteAccount(
        @Body body: DeleteAccountRequest
    )

    @POST("/api/v1/verified-areas")
    suspend fun verifyArea(
        @Body request: AreaVerificationRequest
    )

    @GET("/api/v1/verified-areas")
    suspend fun fetchVerifiedAreaList() : VerifiedAreaListResponse

    @POST("/api/v1/verified-areas/replacements")
    suspend fun replaceVerifiedArea(
        @Body request: ReplaceVerifiedAreaRequest
    )

    @DELETE("/api/v1/verified-areas/{verifiedAreaId}")
    suspend fun deleteVerifiedArea(
        @Path("verifiedAreaId") verifiedAreaId: Long
    )
}