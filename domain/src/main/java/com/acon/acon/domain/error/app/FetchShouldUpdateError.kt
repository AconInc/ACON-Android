package com.acon.acon.domain.error.app

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class FetchShouldUpdateError : RootError() {

    class InvalidPlatform : FetchShouldUpdateError() {
        override val code: Int = 40045
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(InvalidPlatform())
        }
    }
}