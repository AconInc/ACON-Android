package com.acon.acon.domain.repository

import com.acon.acon.domain.model.upload.DotoriCount
import com.acon.acon.domain.model.upload.KeyWord
import com.acon.acon.domain.model.upload.SpotVerification
import com.acon.acon.domain.model.upload.Suggestions

interface UploadRepository {
    suspend fun getDotoriCount(): Result<DotoriCount>

    suspend fun getKeyWord(keyword: String): Result<KeyWord>

    suspend fun getSuggestions(latitude: Double, longitude: Double): Result<Suggestions>

    suspend fun getVerifySpotLocation(
        spotId: Long,
        latitude: Double,
        longitude: Double
    ): Result<SpotVerification>

    suspend fun postReview(
        spotId: Long,
        acornCount: Int
    ): Result<Unit>
}
