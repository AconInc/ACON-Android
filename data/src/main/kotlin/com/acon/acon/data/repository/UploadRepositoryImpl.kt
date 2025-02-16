package com.acon.acon.data.repository

import com.acon.acon.data.datasource.remote.UploadRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.model.upload.DotoriCount
import com.acon.acon.domain.model.upload.KeyWord
import com.acon.acon.domain.model.upload.SpotVerification
import com.acon.acon.domain.model.upload.Suggestions
import com.acon.acon.domain.repository.UploadRepository
import javax.inject.Inject

class UploadRepositoryImpl @Inject constructor(
    private val uploadRemoteDataSource: UploadRemoteDataSource
) : UploadRepository {
    override suspend fun getDotoriCount(): Result<DotoriCount> = runCatchingWith {
        uploadRemoteDataSource.getDotoriCount().toDotoriCount()
    }

    override suspend fun getKeyWord(keyword: String): Result<KeyWord> = runCatchingWith {
        uploadRemoteDataSource.getKeyWord(keyword).toKeyWord()
    }

    override suspend fun getSuggestions(
        latitude: Double,
        longitude: Double
    ): Result<Suggestions> = runCatchingWith {
        uploadRemoteDataSource.getSuggestions(latitude, longitude).toSuggestions()
    }

    override suspend fun getVerifySpotLocation(
        spotId: Long,
        latitude: Double,
        longitude: Double
    ): Result<SpotVerification> = runCatchingWith {
        uploadRemoteDataSource.getVerifySpotLocation(
            spotId = spotId,
            latitude = latitude,
            longitude = longitude
        ).toSpotVerification()
    }

    override suspend fun postReview(
        spotId: Long,
        acornCount: Int
    ): Result<Unit> = runCatchingWith {
        uploadRemoteDataSource.postReview(
            spotId = spotId,
            acornCount = acornCount
        )
    }
}
