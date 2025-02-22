package com.acon.acon.domain.error.user

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class GetSuggestionsError : RootError() {
    class OutOfServiceAreaError : GetSuggestionsError() {
        override val code: Int = 40405
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                OutOfServiceAreaError()
            )
        }
    }
}