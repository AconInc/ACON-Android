package com.acon.acon.data.datasource.remote

import com.acon.acon.data.api.remote.MapSearchApi
import javax.inject.Inject

class MapSearchRemoteDataSource @Inject constructor(
    private val mapSearchApi: MapSearchApi
) {
    suspend fun fetchMapSearch(
        query: String
    ) = mapSearchApi.fetchMapSearch(query)
}