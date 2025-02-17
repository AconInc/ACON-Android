package com.acon.acon.feature.onboarding.screen.PrefResultLoadingScreen

import android.util.Log
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PrefResultLoadingScreenViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : BaseContainerHost<PrefResultLoadingScreenState, PrefResultLoadingScreenSideEffect>() {

    override val container = container<PrefResultLoadingScreenState, PrefResultLoadingScreenSideEffect>(
        PrefResultLoadingScreenState.Loading
    ) { }

    init {
        observeOnboardingResult()
    }

    private fun observeOnboardingResult() = intent {
        onboardingRepository.onboardingResultStateFlow.collect { result ->
            when {
                result?.isSuccess == true -> {
                    delay(1000L)
                    reduce { PrefResultLoadingScreenState.LoadSucceed }
                    delay(2000L)
                    navigateToSpotListView()
                }
                result?.isFailure == true -> {
                    reduce { PrefResultLoadingScreenState.LoadFailed }

                    val throwable = result.exceptionOrNull()
                    when (throwable) {
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidDislikeFood -> Log.e(
                            "OnboardingError",
                            "Invalid Dislike Food: ${throwable.code}"
                        )
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidCuisine -> Log.e(
                            "OnboardingError",
                            "Invalid Cuisine: ${throwable.code}"
                        )
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidSpotType -> Log.e(
                            "OnboardingError",
                            "Invalid Spot Type: ${throwable.code}"
                        )
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidSpotStyle -> Log.e(
                            "OnboardingError",
                            "Invalid Spot Style: ${throwable.code}"
                        )
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidSpotRank -> Log.e(
                            "OnboardingError",
                            "Invalid Spot Rank: ${throwable.code}"
                        )
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidFavSpotRankSize -> Log.e(
                            "OnboardingError",
                            "Invalid Favorite Spot Rank Size: ${throwable.code}"
                        )
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidFavCuisineRankSize -> Log.e(
                            "OnboardingError",
                            "Invalid Favorite Cuisine Rank Size: ${throwable.code}"
                        )
                        else -> Log.e(
                            "OnboardingError",
                            "Unexpected Error: ${throwable?.localizedMessage}",
                            throwable
                        )
                    }
                }
                else -> {  }
            }
        }
    }

    fun navigateToSpotListView() = intent {
        postSideEffect(PrefResultLoadingScreenSideEffect.navigateToSpotListView)
    }

}

sealed interface PrefResultLoadingScreenState {
    data object Loading : PrefResultLoadingScreenState
    data object LoadSucceed : PrefResultLoadingScreenState
    data object LoadFailed: PrefResultLoadingScreenState
}

sealed interface PrefResultLoadingScreenSideEffect {
    data object navigateToSpotListView: PrefResultLoadingScreenSideEffect
}