package com.acon.acon.data.datasource.remote

import com.acon.acon.data.api.remote.UserApi
import com.acon.acon.data.dto.request.AreaVerificationRequest
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.ReplaceVerifiedAreaRequest
import com.acon.acon.data.dto.request.SignInRequest
import com.acon.acon.data.dto.request.SignOutRequest
import com.acon.acon.data.dto.response.SignInResponse
import com.acon.acon.data.dto.response.area.VerifiedAreaListResponse
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApi: UserApi
) {
    suspend fun signIn(googleSignInRequest: SignInRequest): SignInResponse {
        return userApi.postSignIn(googleSignInRequest)
    }

    suspend fun signOut(signOutRequest: SignOutRequest) {
        return userApi.postLogout(signOutRequest)
    }

    suspend fun deleteAccount(deleteAccountRequest: DeleteAccountRequest) {
        return userApi.postDeleteAccount(deleteAccountRequest)
    }

    suspend fun verifyArea(
        latitude: Double,
        longitude: Double
    ) {
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

    suspend fun replaceVerifiedArea(
        previousVerifiedAreaId: Long,
        latitude: Double,
        longitude: Double
    ) {
        return userApi.replaceVerifiedArea(
            request = ReplaceVerifiedAreaRequest(
                previousVerifiedAreaId = previousVerifiedAreaId,
                latitude = latitude,
                longitude = longitude
            )
        )
    }

    suspend fun deleteVerifiedArea(verifiedAreaId: Long) {
        return userApi.deleteVerifiedArea(verifiedAreaId)
    }
}