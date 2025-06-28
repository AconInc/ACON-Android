package com.acon.acon.data.datasource.remote

import com.acon.acon.data.api.remote.UserApi
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.data.dto.request.SignInRequest
import com.acon.acon.data.dto.request.SignOutRequest
import com.acon.acon.data.dto.response.SignInResponse
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
}