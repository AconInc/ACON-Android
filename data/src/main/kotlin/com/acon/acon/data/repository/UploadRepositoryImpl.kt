package com.acon.acon.data.repository

import com.acon.acon.data.datasource.remote.UploadRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.upload.GetVerifySpotLocationError
import com.acon.acon.domain.error.upload.UploadReviewError
import com.acon.acon.domain.error.user.GetSuggestionsError
import com.acon.acon.domain.model.upload.DotoriCount
import com.acon.acon.domain.model.upload.SpotVerification
import com.acon.acon.domain.model.upload.UploadSpotSuggestion
import com.acon.acon.domain.model.upload.v2.SearchedSpot
import com.acon.acon.domain.repository.UploadRepository
import javax.inject.Inject

class UploadRepositoryImpl @Inject constructor(
    private val uploadRemoteDataSource: UploadRemoteDataSource
) : UploadRepository {
    override suspend fun getDotoriCount(): Result<DotoriCount> = runCatchingWith {
        uploadRemoteDataSource.getDotoriCount().toDotoriCount()
    }

    override suspend fun getSuggestions(
        latitude: Double,
        longitude: Double
    ): Result<List<UploadSpotSuggestion>> = runCatchingWith(*GetSuggestionsError.createErrorInstances()) {
        uploadRemoteDataSource.getSuggestions(latitude, longitude).suggestionList.map { it.toSuggestion() }
    }

    override suspend fun getVerifySpotLocation(
        spotId: Long,
        latitude: Double,
        longitude: Double
    ): Result<SpotVerification> = runCatchingWith(*GetVerifySpotLocationError.createErrorInstances()) {
        uploadRemoteDataSource.getVerifySpotLocation(
            spotId = spotId,
            latitude = latitude,
            longitude = longitude
        ).toSpotVerification()
    }

    override suspend fun postReview(
        spotId: Long,
        acornCount: Int
    ): Result<Unit> = runCatchingWith(*UploadReviewError.createErrorInstances()) {
        uploadRemoteDataSource.postReview(
            spotId = spotId,
            acornCount = acornCount
        )
    }

    override suspend fun getSearchedSpots(query: String): Result<List<SearchedSpot>> {
        return runCatchingWith {
            uploadRemoteDataSource.getSearchedSpots(query).searchedSpots.map { it.toSearchedSpot() }
        }
    }
}
