package com.acon.acon.domain.error.app

import com.acon.acon.domain.error.RootError

open class FetchShouldUpdateError : RootError() {

    class InvalidPlatform : FetchShouldUpdateError() {
        override val code: Int = 40045
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(InvalidPlatform())
    }
}