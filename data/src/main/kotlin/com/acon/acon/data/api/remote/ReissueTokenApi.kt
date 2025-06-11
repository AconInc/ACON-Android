package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.request.RefreshRequest
import com.acon.acon.data.dto.response.RefreshResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ReissueTokenApi {

    @POST("/api/v1/auth/reissue")
    suspend fun postRefresh(
        @Body refreshRequest: RefreshRequest
    ) : RefreshResponse
}