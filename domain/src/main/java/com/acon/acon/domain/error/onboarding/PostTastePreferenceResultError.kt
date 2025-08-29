package com.acon.acon.domain.error.onboarding

import com.acon.acon.domain.error.RootError

open class PostTastePreferenceResultError : RootError() {

    class InvalidDislikeFood : PostTastePreferenceResultError() {
        override val code: Int = 40013
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            InvalidDislikeFood()
        )
    }
}