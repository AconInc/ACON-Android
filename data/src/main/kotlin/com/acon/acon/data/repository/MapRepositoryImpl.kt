package com.acon.acon.data.repository

import com.acon.acon.data.datasource.remote.MapRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.repository.MapRepository
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val mapRemoteDataSource: MapRemoteDataSource
) : MapRepository {

    override suspend fun fetchLegalAddressName(
        latitude: Double,
        longitude: Double
    ): Result<String> {
        return runCatchingWith {
            mapRemoteDataSource.fetchReverseGeocoding(latitude, longitude).results.filter {
                it.name == "legalcode"
            }.firstOrNull()?.region?.area3?.name ?: ""
        }
    }

    override suspend fun fetchDirections(
        startLatitude: Double,
        startLongitude: Double,
        goalLatitude: Double,
        goalLongitude: Double
    ): Result<Int> {
        return runCatchingWith {
            mapRemoteDataSource.fetchDirections(startLatitude, startLongitude, goalLatitude, goalLongitude)
                .route
                .trafast
                .firstOrNull()?.summary?.duration ?: 0
        }
    }
}