package com.acon.acon.data.datasource.remote

import com.acon.acon.data.api.remote.auth.UploadAuthApi
import com.acon.acon.data.dto.request.ReviewRequest
import com.acon.acon.data.dto.response.profile.PreSignedUrlResponse
import com.acon.acon.data.dto.response.upload.UploadSpotSuggestionsResponse
import com.acon.acon.data.dto.response.upload.VerifyLocationResponse
import javax.inject.Inject

class UploadRemoteDataSource @Inject constructor(
    private val uploadAuthApi: UploadAuthApi
) {

    suspend fun getSuggestions(
        latitude: Double,
        longitude: Double
    ): UploadSpotSuggestionsResponse {
        return uploadAuthApi.getSuggestions(latitude, longitude)
    }

    suspend fun verifyLocation(
        spotId: Long,
        latitude: Double,
        longitude: Double
    ): VerifyLocationResponse {
        return uploadAuthApi.verifyLocation(spotId, latitude, longitude)
    }

    suspend fun submitReview(
        spotId: Long,
        acornCount: Int
    ) = uploadAuthApi.submitReview(
        ReviewRequest(
            spotId = spotId,
            acornCount = acornCount
        )
    )

    suspend fun getSearchedSpots(
        query: String
    ) = uploadAuthApi.getSearchedSpots(query)


    suspend fun getUploadPlacePreSignedUrl(): PreSignedUrlResponse {
        return uploadAuthApi.getUploadPlacePreSignedUrl()
    }
}
