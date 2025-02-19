package com.acon.acon.domain.repository

import com.acon.acon.domain.model.area.Area
import com.acon.acon.domain.model.area.SettingsVerifiedArea

interface AreaVerificationRepository {
   suspend fun verifyArea(
       latitude: Double,
       longitude: Double
   ): Result<Area>

   suspend fun fetchVerifiedAreaList(): Result<List<SettingsVerifiedArea>>
   suspend fun deleteVerifiedArea(verifiedAreaId: Long): Result<Unit>
}