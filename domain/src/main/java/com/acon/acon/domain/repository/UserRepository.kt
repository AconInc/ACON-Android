package com.acon.acon.domain.repository

import com.acon.acon.domain.model.area.Area
import com.acon.acon.domain.model.user.VerificationStatus
import com.acon.acon.domain.type.SocialType
import com.acon.acon.domain.type.UserType
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signIn(socialType: SocialType, idToken: String): Result<VerificationStatus>
    suspend fun logout(): Result<Unit>
    suspend fun deleteAccount(reason: String): Result<Unit>
    fun getUserType(): Flow<UserType>

    suspend fun verifyArea(
        latitude: Double,
        longitude: Double
    ): Result<Unit>

    suspend fun fetchVerifiedAreaList(): Result<List<Area>>

    suspend fun replaceVerifiedArea(
        previousVerifiedAreaId: Long,
        latitude: Double,
        longitude: Double
    ): Result<Unit>

    suspend fun deleteVerifiedArea(verifiedAreaId: Long): Result<Unit>
}