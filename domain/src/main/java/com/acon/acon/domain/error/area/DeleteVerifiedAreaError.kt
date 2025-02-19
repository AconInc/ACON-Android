package com.acon.acon.domain.error.area

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError
import com.acon.acon.domain.error.spot.FetchRecentNavigationLocationError.SpaceNotFoundError

sealed class DeleteVerifiedAreaError : RootError() {

    class InvalidVerifiedArea : DeleteVerifiedAreaError() {
        override val code: Int = 40054
    }

    class VerifiedAreaLimitViolation : DeleteVerifiedAreaError() {
        override val code: Int = 40032
    }

    class VerifiedAreaNotFound : DeleteVerifiedAreaError() {
        override val code: Int = 40404
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                SpaceNotFoundError(),
            )
        }
    }
}