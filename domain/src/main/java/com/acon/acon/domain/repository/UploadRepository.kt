package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.profile.PreSignedUrl
import com.acon.acon.core.model.model.upload.SearchedSpot

interface UploadRepository {
    suspend fun getSuggestions(latitude: Double, longitude: Double): Result<List<com.acon.acon.core.model.model.upload.UploadSpotSuggestion>>

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

    suspend fun getUploadPlacePreSignedUrl(): Result<PreSignedUrl>
}
