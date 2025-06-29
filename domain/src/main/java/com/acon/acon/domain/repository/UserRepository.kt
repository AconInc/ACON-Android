package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.area.Area
import com.acon.acon.core.model.model.user.VerificationStatus
import com.acon.acon.core.model.type.SocialType
import com.acon.acon.core.model.type.UserType
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signIn(socialType: com.acon.acon.core.model.type.SocialType, idToken: String): Result<com.acon.acon.core.model.model.user.VerificationStatus>
    suspend fun logout(): Result<Unit>
    suspend fun deleteAccount(reason: String): Result<Unit>
    fun getUserType(): Flow<com.acon.acon.core.model.type.UserType>

    suspend fun verifyArea(
        latitude: Double,
        longitude: Double
    ): Result<Unit>

    suspend fun fetchVerifiedAreaList(): Result<List<com.acon.acon.core.model.model.area.Area>>

    suspend fun replaceVerifiedArea(
        previousVerifiedAreaId: Long,
        latitude: Double,
        longitude: Double
    ): Result<Unit>

    suspend fun deleteVerifiedArea(verifiedAreaId: Long): Result<Unit>
}