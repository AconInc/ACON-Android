package com.acon.core.data.api.remote.auth

import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.core.data.dto.request.SignOutRequest
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
