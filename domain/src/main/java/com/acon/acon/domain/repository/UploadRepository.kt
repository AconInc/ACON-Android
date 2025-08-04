package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.profile.PreSignedUrl
import com.acon.acon.core.model.model.upload.Feature
import com.acon.acon.core.model.model.upload.SearchedSpot
import com.acon.acon.core.model.model.upload.UploadSpotSuggestion
import com.acon.acon.core.model.type.SpotType

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

    suspend fun submitReviewV2(
        spotId: Long,
        recommendedMenu: String,
        acornCount: Int
    ): Result<Unit>


    suspend fun getSearchedSpots(
        query: String
    ): Result<List<SearchedSpot>>

    suspend fun getUploadPlacePreSignedUrl(): Result<PreSignedUrl>


    suspend fun submitUploadPlace(
        spotName: String,
        address: String,
        spotType: SpotType,
        featureList: List<Feature>,
        recommendedMenu: String,
        imageList: List<String>?
    ): Result<Unit>
}
