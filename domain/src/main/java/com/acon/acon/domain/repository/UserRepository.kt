package com.acon.acon.domain.repository

import com.acon.acon.domain.model.user.VerificationStatus
import com.acon.acon.domain.type.SocialType
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    fun getLoginState(): StateFlow<Boolean>
    suspend fun postLogin(socialType: SocialType, idToken: String): Result<VerificationStatus>
    suspend fun postLogout(refreshToken: String): Result<Unit>
    suspend fun postDeleteAccount(reason: String, refreshToken: String): Result<Unit>
}