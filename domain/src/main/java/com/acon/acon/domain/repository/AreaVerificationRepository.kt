package com.acon.acon.domain.repository

import com.acon.acon.domain.model.area.Area

interface AreaVerificationRepository {
   suspend fun verifyArea(
       latitude: Double,
       longitude: Double
   ): Result<Area>
}