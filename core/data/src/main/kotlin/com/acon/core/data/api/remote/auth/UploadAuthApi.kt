package com.acon.core.data.api.remote.auth

import com.acon.core.data.dto.request.ReviewRequest
import com.acon.core.data.dto.request.ReviewRequestV2
import com.acon.core.data.dto.request.SubmitUploadPlaceRequest
import com.acon.core.data.dto.response.profile.PreSignedUrlResponse
import com.acon.acon.data.dto.response.upload.SearchedSpotsResponse
import com.acon.acon.data.dto.response.upload.UploadSpotSuggestionsResponse
import com.acon.acon.data.dto.response.upload.VerifyLocationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UploadAuthApi {

    @GET("/api/v2/spots/search-suggestions")
    suspend fun getSuggestions(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): UploadSpotSuggestionsResponse

    @GET("/api/v2/reviews/verify")
    suspend fun verifyLocation(
        @Query("spotId") spotId: Long,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): VerifyLocationResponse

    @POST("/api/v1/reviews")
    suspend fun submitReview(
        @Body request: ReviewRequest
    )

    @POST("/api/v2/reviews")
    suspend fun submitReviewV2(
        @Body request: ReviewRequestV2
    )

    @GET("/api/v1/spots/search")
    suspend fun getSearchedSpots(@Query("keyword") query: String): SearchedSpotsResponse

    @GET("/api/v1/images/presigned-url?imageType=APPLY_SPOT")
    suspend fun getUploadPlacePreSignedUrl() : PreSignedUrlResponse

    @POST("/api/v1/spots/apply")
    suspend fun submitUploadPlace(@Body request: SubmitUploadPlaceRequest)
}
