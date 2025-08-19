package com.acon.acon.domain.error.user

import com.acon.acon.domain.error.RootError

open class GetSuggestionsError : RootError() {
    class OutOfServiceAreaError : GetSuggestionsError() {
        override val code: Int = 40405
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            OutOfServiceAreaError()
        )
    }
}