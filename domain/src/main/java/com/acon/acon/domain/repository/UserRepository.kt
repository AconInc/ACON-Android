package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.user.CredentialCode
import com.acon.acon.core.model.model.user.SocialPlatform
import com.acon.acon.core.model.model.user.VerificationStatus
import com.acon.acon.core.model.type.UserType
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signIn(socialType: SocialPlatform, code: CredentialCode): Result<VerificationStatus>
    suspend fun signOut(): Result<Unit>
    suspend fun deleteAccount(reason: String): Result<Unit>
    suspend fun clearSession(): Result<Unit>
    suspend fun saveDidOnboarding(didOnboarding: Boolean): Result<Unit>
    suspend fun getDidOnboarding(): Result<Boolean>
    fun getUserType(): Flow<UserType>
}