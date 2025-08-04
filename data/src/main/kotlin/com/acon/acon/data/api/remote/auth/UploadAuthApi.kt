package com.acon.acon.data.api.remote.auth

import com.acon.acon.data.dto.request.ReviewRequest
import com.acon.acon.data.dto.request.ReviewRequestV2
import com.acon.acon.data.dto.request.SubmitUploadPlaceRequest
import com.acon.acon.data.dto.response.profile.PreSignedUrlResponse
import com.acon.acon.data.dto.response.upload.SearchedSpotsResponse
import com.acon.acon.data.dto.response.upload.UploadSpotSuggestionsResponse
import com.acon.acon.data.dto.response.upload.VerifyLocationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UploadAuthApi {

    @GET("/api/v1/spots/search-suggestions")
    suspend fun getSuggestions(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): UploadSpotSuggestionsResponse

    @GET("/api/v1/reviews/verify")
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

    @GET("/api/v2/images/presigned-url?imageType=SPOT")
    suspend fun getUploadPlacePreSignedUrl() : PreSignedUrlResponse

    @GET("/api/v1/spots/apply")
    suspend fun submitUploadPlace(@Body request: SubmitUploadPlaceRequest)
}
