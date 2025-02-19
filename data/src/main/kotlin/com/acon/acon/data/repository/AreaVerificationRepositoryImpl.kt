package com.acon.acon.data.repository

import com.acon.acon.data.datasource.remote.AreaVerificationRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.area.DeleteVerifiedAreaError
import com.acon.acon.domain.model.area.Area
import com.acon.acon.domain.model.area.SettingsVerifiedArea
import com.acon.acon.domain.repository.AreaVerificationRepository
import javax.inject.Inject

class AreaVerificationRepositoryImpl @Inject constructor(
   private val remoteDataSource: AreaVerificationRemoteDataSource
) : AreaVerificationRepository {

   override suspend fun verifyArea(
       latitude: Double,
       longitude: Double
   ): Result<Area> = runCatchingWith {
       remoteDataSource.verifyArea(
           latitude = latitude,
           longitude = longitude
       ).toArea()
   }

    override suspend fun fetchVerifiedAreaList(): Result<List<SettingsVerifiedArea>> {
        return runCatchingWith() {
            remoteDataSource.fetchVerifiedAreaList().verifiedAreaList
                .map { it.toVerifiedArea() }
        }
    }

    override suspend fun deleteVerifiedArea(verifiedAreaId: Long): Result<Unit> {
        return runCatchingWith(*DeleteVerifiedAreaError.createErrorInstances()) {
            remoteDataSource.deleteVerifiedArea(verifiedAreaId)
        }
    }
}