package com.acon.acon.domain.error.spot

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class FetchRecentNavigationLocationError : RootError() {

    class SpaceNotFoundError : FetchRecentNavigationLocationError() {
        override val code: Int = 40403
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                SpaceNotFoundError(),
            )
        }
    }
}