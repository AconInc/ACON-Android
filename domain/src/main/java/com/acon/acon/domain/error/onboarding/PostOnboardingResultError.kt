package com.acon.acon.domain.error.onboarding

import com.acon.acon.domain.error.RootError
import com.acon.acon.domain.error.ErrorFactory

sealed class PostOnboardingResultError : RootError() {

    class InvalidDislikeFood : PostOnboardingResultError() {
        override val code: Int = 40013
    }
    class InvalidCuisine : PostOnboardingResultError() {
        override val code: Int = 40014
    }
    class InvalidSpotType : PostOnboardingResultError() {
        override val code: Int = 40015
    }
    class InvalidSpotStyle : PostOnboardingResultError() {
        override val code: Int = 40016
    }
    class InvalidSpotRank : PostOnboardingResultError() {
        override val code: Int = 40017
    }
    class InvalidFavSpotRankSize : PostOnboardingResultError() {
        override val code: Int = 40030
    }
    class InvalidFavCuisineRankSize : PostOnboardingResultError() {
        override val code: Int = 40031
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                InvalidDislikeFood(),
                InvalidCuisine(),
                InvalidSpotType(),
                InvalidSpotStyle(),
                InvalidSpotRank(),
                InvalidFavSpotRankSize(),
                InvalidFavCuisineRankSize()
            )
        }
    }
}