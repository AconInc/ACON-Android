package com.acon.acon.data.api.remote.noauth

import com.acon.acon.data.dto.request.ReissueRequest
import com.acon.acon.data.dto.request.SignInRequest
import com.acon.acon.data.dto.response.SignInResponse
import com.acon.acon.data.dto.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

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