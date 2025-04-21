package com.acon.acon.domain.repository

import com.acon.acon.domain.model.user.VerificationStatus
import com.acon.acon.domain.type.SocialType
import com.acon.acon.domain.type.UserType
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(socialType: SocialType, idToken: String): Result<VerificationStatus>
    suspend fun logout(refreshToken: String): Result<Unit>
    suspend fun deleteAccount(reason: String, refreshToken: String): Result<Unit>
    fun getUserType(): Flow<UserType>
}