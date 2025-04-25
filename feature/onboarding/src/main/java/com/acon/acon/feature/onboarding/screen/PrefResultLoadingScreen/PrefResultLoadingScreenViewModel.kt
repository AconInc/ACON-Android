package com.acon.acon.feature.onboarding.screen.PrefResultLoadingScreen

import android.util.Log
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
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
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidDislikeFood -> Timber.tag(
                            "OnboardingError"
                        ).e("Invalid Dislike Food: ${throwable.code}")
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidCuisine -> Timber.tag(
                            "OnboardingError"
                        ).e("Invalid Cuisine: ${throwable.code}")
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidSpotType -> Timber.tag(
                            "OnboardingError"
                        ).e("Invalid Spot Type: ${throwable.code}")
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidSpotStyle -> Timber.tag(
                            "OnboardingError"
                        ).e("Invalid Spot Style: ${throwable.code}")
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidSpotRank -> Timber.tag(
                            "OnboardingError"
                        ).e("Invalid Spot Rank: ${throwable.code}")
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidFavSpotRankSize -> Timber.tag(
                            "OnboardingError"
                        ).e("Invalid Favorite Spot Rank Size: ${throwable.code}")
                        is com.acon.acon.domain.error.onboarding.PostOnboardingResultError.InvalidFavCuisineRankSize -> Timber.tag(
                            "OnboardingError"
                        ).e("Invalid Favorite Cuisine Rank Size: ${throwable.code}")
                        else -> Timber.tag("OnboardingError")
                            .e(throwable, "Unexpected Error: ${throwable?.localizedMessage}")
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