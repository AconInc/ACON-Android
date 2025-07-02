package com.acon.acon.domain.error.onboarding

import com.acon.acon.domain.error.RootError

open class PostOnboardingResultError : RootError() {

    class InvalidDislikeFood : PostOnboardingResultError() {
        override val code: Int = 40013
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            InvalidDislikeFood()
        )
    }
}