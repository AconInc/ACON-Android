package com.acon.core.data.datasource.remote

import com.acon.core.data.api.remote.MapApi
import javax.inject.Inject

class MapRemoteDataSource @Inject constructor(
    private val mapApi: MapApi
) {
    suspend fun fetchReverseGeocoding(
        latitude: Double,
        longitude: Double
    ) = mapApi.fetchReverseGeocoding("$longitude,$latitude")
}