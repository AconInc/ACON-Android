package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.LoginRequest
import com.acon.acon.data.dto.request.LogoutRequest
import com.acon.acon.data.dto.response.LoginResponse
import com.acon.acon.data.api.remote.UserApi
import com.acon.acon.data.dto.request.AreaVerificationRequest
import com.acon.acon.data.dto.response.area.AreaVerificationResponse
import com.acon.acon.data.dto.response.area.VerifiedAreaListResponse
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApi: UserApi
) {
    suspend fun login(googleLoginRequest: LoginRequest): LoginResponse {
        return userApi.postLogin(googleLoginRequest)
    }

    suspend fun logout(logoutRequest: LogoutRequest) {
        return userApi.postLogout(logoutRequest)
    }

    suspend fun deleteAccount(deleteAccountRequest: DeleteAccountRequest) {
        return userApi.postDeleteAccount(deleteAccountRequest)
    }

    suspend fun verifyArea(
        latitude: Double,
        longitude: Double
    ): AreaVerificationResponse {
        return userApi.verifyArea(
            request = AreaVerificationRequest(
                latitude = latitude,
                longitude = longitude
            )
        )
    }

    suspend fun fetchVerifiedAreaList() : VerifiedAreaListResponse {
        return userApi.fetchVerifiedAreaList()
    }

    suspend fun deleteVerifiedArea(verifiedAreaId: Long) {
        return userApi.deleteVerifiedArea(verifiedAreaId)
    }
}