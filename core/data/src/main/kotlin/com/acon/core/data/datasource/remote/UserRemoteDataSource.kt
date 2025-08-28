package com.acon.core.data.datasource.remote

import com.acon.core.data.api.remote.auth.UserAuthApi
import com.acon.core.data.api.remote.noauth.UserNoAuthApi
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.core.data.dto.request.SignInRequest
import com.acon.core.data.dto.request.SignOutRequest
import com.acon.core.data.dto.response.SignInResponse
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userAuthApi: UserAuthApi,
    private val userNoAuthApi: UserNoAuthApi
) {
    suspend fun signIn(googleSignInRequest: SignInRequest): SignInResponse {
        return userNoAuthApi.postSignIn(googleSignInRequest)
    }

    suspend fun signOut(signOutRequest: SignOutRequest) {
        return userAuthApi.postLogout(signOutRequest)
    }

    suspend fun deleteAccount(deleteAccountRequest: DeleteAccountRequest) {
        return userAuthApi.postDeleteAccount(deleteAccountRequest)
    }
}