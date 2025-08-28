package com.acon.core.data.datasource.remote

import com.acon.core.data.api.remote.MapSearchApi
import javax.inject.Inject

class MapSearchRemoteDataSource @Inject constructor(
    private val mapSearchApi: MapSearchApi
) {
    suspend fun fetchMapSearch(
        query: String
    ) = mapSearchApi.fetchMapSearch(query)
}