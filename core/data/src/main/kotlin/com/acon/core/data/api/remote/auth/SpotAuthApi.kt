package com.acon.core.data.api.remote.auth

import com.acon.core.data.dto.request.AddBookmarkRequest
import com.acon.core.data.dto.request.SpotListRequest
import com.acon.core.data.dto.response.SpotDetailResponse
import com.acon.core.data.dto.response.SpotListResponse
import com.acon.core.data.dto.response.profile.SavedSpotsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotAuthApi {

    @POST("/api/v1/spots")
    suspend fun fetchSpotList(
        @Body request: SpotListRequest
    ): SpotListResponse

    @GET("/api/v1/saved-spots")
    suspend fun fetchSavedSpotList(): SavedSpotsResponse

    @POST("/api/v1/saved-spots")
    suspend fun addBookmark(
        @Body addBookmarkRequest: AddBookmarkRequest
    )

    @GET("/api/v1/spots/{spotId}")
    suspend fun fetchSpotDetailFromUser(
        @Path("spotId") spotId: Long,
        @Query("isDeepLink") isDeepLink: Boolean = false
    ): SpotDetailResponse

    @DELETE("/api/v1/saved-spots/{spotId}")
    suspend fun deleteBookmark(
        @Path("spotId") spotId: Long
    )
}