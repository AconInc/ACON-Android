package com.acon.acon.domain.error.profile

import com.acon.acon.domain.error.RootError

open class SaveSpotError : RootError() {

    class NotExistSpot : SaveSpotError() {
        override val code: Int = 40403
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            NotExistSpot(),
        )
    }
}