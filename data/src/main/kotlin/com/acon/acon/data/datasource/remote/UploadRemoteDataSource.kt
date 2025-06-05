package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.ReviewRequest
import com.acon.acon.data.dto.response.upload.UploadSpotSuggestionsResponse
import com.acon.acon.data.dto.response.upload.VerifyLocationResponse
import com.acon.acon.data.remote.UploadApi
import javax.inject.Inject

class UploadRemoteDataSource @Inject constructor(
    private val uploadApi: UploadApi
) {

    suspend fun getSuggestions(
        latitude: Double,
        longitude: Double
    ): UploadSpotSuggestionsResponse {
        return uploadApi.getSuggestions(latitude, longitude)
    }

    suspend fun verifyLocation(
        spotId: Long,
        latitude: Double,
        longitude: Double
    ): VerifyLocationResponse {
        return uploadApi.verifyLocation(spotId, latitude, longitude)
    }

    suspend fun submitReview(
        spotId: Long,
        acornCount: Int
    ) = uploadApi.submitReview(
        ReviewRequest(
            spotId = spotId,
            acornCount = acornCount
        )
    )

    suspend fun getSearchedSpots(
        query: String
    ) = uploadApi.getSearchedSpots(query)
}
