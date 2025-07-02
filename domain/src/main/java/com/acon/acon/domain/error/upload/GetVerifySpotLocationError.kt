package com.acon.acon.domain.error.upload

import com.acon.acon.domain.error.RootError

open class GetVerifySpotLocationError : RootError() {
    class NotExistSpot : GetVerifySpotLocationError() {
        override val code: Int = 40403
    }
    class OutOfServiceAreaError : GetVerifySpotLocationError() {
        override val code: Int = 40405
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            NotExistSpot(),
            OutOfServiceAreaError()
        )
    }
}