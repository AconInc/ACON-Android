package com.acon.acon.data.datasource.remote

import com.acon.acon.data.remote.MapApi
import javax.inject.Inject

class MapRemoteDataSource @Inject constructor(
    private val mapApi: MapApi
) {
    suspend fun fetchReverseGeocoding(
        latitude: Double,
        longitude: Double
    ) = mapApi.fetchReverseGeocoding("$longitude,$latitude")

    suspend fun fetchDirections(
        startLatitude: Double,
        startLongitude: Double,
        goalLatitude: Double,
        goalLongitude: Double
    ) = mapApi.fetchDirections(
        start = "$startLatitude,$startLongitude",
        goal = "$goalLatitude,$goalLongitude",
    )
}