package com.acon.acon.domain.error.onboarding

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class PostOnboardingResultError : RootError() {

    class InvalidDislikeFood : PostOnboardingResultError() {
        override val code: Int = 40013
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                InvalidDislikeFood()
            )
        }
    }
}