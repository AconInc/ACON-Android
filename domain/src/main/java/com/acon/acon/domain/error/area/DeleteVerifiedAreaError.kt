package com.acon.acon.domain.error.area

import com.acon.acon.domain.error.RootError

open class DeleteVerifiedAreaError : RootError() {

    class InvalidVerifiedArea : DeleteVerifiedAreaError() {
        override val code: Int = 40054
    }
    class VerifiedAreaLimitViolation : DeleteVerifiedAreaError() {
        override val code: Int = 40032
    }
    class PeriodRestrictedDeleteError : DeleteVerifiedAreaError() {
        override val code: Int = 40055
    }
    class VerifiedAreaNotFound : DeleteVerifiedAreaError() {
        override val code: Int = 40404
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            InvalidVerifiedArea(),
            VerifiedAreaLimitViolation(),
            PeriodRestrictedDeleteError(),
            VerifiedAreaNotFound()
        )
    }
}