package com.acon.domain.error.spot

import com.acon.domain.error.ErrorFactory
import com.acon.domain.error.RootError

sealed class GetSpotDetailInfoError : RootError() {
    class SpaceNotFoundError : GetSpotDetailInfoError() {
        override val code: Int = 40403
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                SpaceNotFoundError()
            )
        }
    }
}