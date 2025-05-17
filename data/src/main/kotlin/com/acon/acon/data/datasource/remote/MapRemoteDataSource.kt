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
}