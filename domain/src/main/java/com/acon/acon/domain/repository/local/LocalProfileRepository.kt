package com.acon.acon.domain.repository.local

import com.acon.acon.domain.type.UpdateProfileType
import kotlinx.coroutines.flow.Flow

interface LocalProfileRepository {
    fun getProfileType(): Flow<UpdateProfileType>
    fun updateProfileType(type: UpdateProfileType)
    suspend fun resetProfileType()
}