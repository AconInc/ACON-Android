package com.acon.acon.data.repository

import android.util.Log
import com.acon.acon.data.datasource.remote.OnboardingRemoteDataSource
import com.acon.acon.data.dto.request.PostOnboardingResultRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.onboarding.PostOnboardingResultError
import com.acon.acon.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingRemoteDataSource: OnboardingRemoteDataSource,
) : OnboardingRepository {

    private val _onboardingResultStateFlow = MutableStateFlow<Result<Unit>?>(null)
    override val onboardingResultStateFlow: StateFlow<Result<Unit>?> = _onboardingResultStateFlow.asStateFlow()

    override suspend fun postOnboardingResult(
        dislikeFoodList: Set<String>,
        favoriteCuisineRank: List<String>,
        favoriteSpotType: String,
        favoriteSpotStyle: String,
        favoriteSpotRank: List<String>
    ): Result<Unit> {
        return runCatchingWith(*PostOnboardingResultError.createErrorInstances()){
            val request = PostOnboardingResultRequest(
                dislikeFoodList = dislikeFoodList,
                favoriteCuisineRank = favoriteCuisineRank,
                favoriteSpotType = favoriteSpotType,
                favoriteSpotStyle = favoriteSpotStyle,
                favoriteSpotRank = favoriteSpotRank
            )

            val requestJson = Json.encodeToString(request)

            val response = onboardingRemoteDataSource.postResult(request)

            if (response.isSuccessful) {
                _onboardingResultStateFlow.emit(Result.success(Unit))
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val exception = RuntimeException("Server error: $errorBody")
                _onboardingResultStateFlow.emit(Result.failure(exception))
                Result.failure(exception)
            }
        }
    }
}