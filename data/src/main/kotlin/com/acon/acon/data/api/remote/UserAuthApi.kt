package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.ReissueRequest
import com.acon.acon.data.dto.request.SignInRequest
import com.acon.acon.data.dto.request.SignOutRequest
import com.acon.acon.data.dto.response.TokenResponse
import com.acon.acon.data.dto.response.SignInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAuthApi {

    @POST("/api/v1/auth/logout")
    suspend fun postLogout(
        @Body body: SignOutRequest
    )

    @POST("/api/v1/members/withdrawal")
    suspend fun postDeleteAccount(
        @Body body: DeleteAccountRequest
    )
}

interface UserNoAuthApi {

    @POST("/api/v1/auth/login")
    suspend fun postSignIn(
        @Body body: SignInRequest
    ) : SignInResponse

    @POST("/api/v1/auth/reissue")
    suspend fun reissueToken(
        @Body reissueRequest: ReissueRequest
    ) : TokenResponse
}