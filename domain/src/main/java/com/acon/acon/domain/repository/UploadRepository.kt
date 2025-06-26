package com.acon.acon.domain.repository

import com.acon.core.model.upload.UploadSpotSuggestion
import com.acon.core.model.upload.SearchedSpot

interface UploadRepository {
    suspend fun getSuggestions(latitude: Double, longitude: Double): Result<List<UploadSpotSuggestion>>

    suspend fun verifyLocation(
        spotId: Long,
        latitude: Double,
        longitude: Double
    ): Result<Boolean>

    suspend fun submitReview(
        spotId: Long,
        acornCount: Int
    ): Result<Unit>

    suspend fun getSearchedSpots(
        query: String
    ): Result<List<SearchedSpot>>
}
