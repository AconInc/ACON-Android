package com.acon.acon.domain.repository

interface MapRepository {

    suspend fun fetchLegalAddressName(
        latitude: Double,
        longitude: Double
    ): Result<String>

    suspend fun fetchDirections(
        startLatitude: Double,
        startLongitude: Double,
        goalLatitude: Double,
        goalLongitude: Double
    ): Result<Int>
}