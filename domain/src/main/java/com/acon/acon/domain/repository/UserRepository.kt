package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.user.VerificationStatus
import com.acon.acon.core.model.type.SocialType
import com.acon.acon.core.model.type.UserType
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signIn(socialType: SocialType, idToken: String): Result<VerificationStatus>
    suspend fun logout(): Result<Unit>
    suspend fun deleteAccount(reason: String): Result<Unit>
    fun getUserType(): Flow<UserType>
}