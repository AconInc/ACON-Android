package com.acon.acon.data.repository

import com.acon.acon.core.model.model.profile.PreSignedUrl
import com.acon.acon.core.model.model.upload.Feature
import com.acon.acon.core.model.model.upload.SearchedSpot
import com.acon.acon.core.model.model.upload.UploadSpotSuggestion
import com.acon.acon.core.model.type.SpotType
import com.acon.acon.data.datasource.remote.UploadRemoteDataSource
import com.acon.acon.data.dto.request.FeatureRequest
import com.acon.acon.data.dto.request.SubmitUploadPlaceRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.upload.GetUploadPlacePreSignedUrlError
import com.acon.acon.domain.error.upload.GetVerifySpotLocationError
import com.acon.acon.domain.error.upload.SubmitUploadPlaceError
import com.acon.acon.domain.error.upload.UploadReviewError
import com.acon.acon.domain.error.user.GetSuggestionsError
import com.acon.acon.domain.repository.UploadRepository
import javax.inject.Inject

class UploadRepositoryImpl @Inject constructor(
    private val uploadRemoteDataSource: UploadRemoteDataSource
) : UploadRepository {

    override suspend fun getSuggestions(
        latitude: Double,
        longitude: Double
    ): Result<List<UploadSpotSuggestion>> = runCatchingWith(GetSuggestionsError()) {
        uploadRemoteDataSource.getSuggestions(latitude, longitude).suggestionList.map { it.toSuggestion() }
    }

    override suspend fun verifyLocation(
        spotId: Long,
        latitude: Double,
        longitude: Double
    ): Result<Boolean> = runCatchingWith(GetVerifySpotLocationError()) {
        uploadRemoteDataSource.verifyLocation(
            spotId = spotId,
            latitude = latitude,
            longitude = longitude
        ).isPossible
    }

    override suspend fun submitReview(
        spotId: Long,
        acornCount: Int
    ): Result<Unit> = runCatchingWith(UploadReviewError()) {
        uploadRemoteDataSource.submitReview(
            spotId = spotId,
            acornCount = acornCount
        )
    }

    override suspend fun submitReviewV2(
        spotId: Long,
        recommendedMenu: String,
        acornCount: Int
    ): Result<Unit> = runCatchingWith(UploadReviewError()) {
        uploadRemoteDataSource.submitReviewV2(
            spotId = spotId,
            recommendedMenu = recommendedMenu,
            acornCount = acornCount
        )
    }

    override suspend fun getSearchedSpots(query: String): Result<List<SearchedSpot>> {
        return runCatchingWith {
            uploadRemoteDataSource.getSearchedSpots(query).searchedSpots.map { it.toSearchedSpot() }
        }
    }

    override suspend fun getUploadPlacePreSignedUrl(): Result<PreSignedUrl> {
        return runCatchingWith(GetUploadPlacePreSignedUrlError()) {
            uploadRemoteDataSource.getUploadPlacePreSignedUrl().toPreSignedUrl()
        }
    }

    override suspend fun submitUploadPlace(
        spotName: String,
        address: String,
        spotType: SpotType,
        featureList: List<Feature>,
        recommendedMenu: String,
        imageList: List<String>?
    ): Result<Unit> {
        return runCatchingWith(SubmitUploadPlaceError()) {
            uploadRemoteDataSource.submitUploadPlace(
                SubmitUploadPlaceRequest(
                    spotName = spotName,
                    address = address,
                    spotType = spotType,
                    featureList = featureList.map { featrue ->
                        FeatureRequest(
                            category = featrue.category.name,
                            optionList = featrue.optionList.map { optionTypes -> optionTypes.getName() }
                        )
                    },
                    recommendedMenu = recommendedMenu,
                    imageList = imageList
                )
            )
        }
    }
}
