package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.upload.SearchedSpotByMap

interface MapSearchRepository {
    suspend fun fetchMapSearch(
        query: String
    ): Result<List<SearchedSpotByMap>>
}