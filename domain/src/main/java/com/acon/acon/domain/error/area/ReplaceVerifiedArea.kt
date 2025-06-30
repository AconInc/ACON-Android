package com.acon.acon.domain.error.area

import com.acon.acon.domain.error.RootError

open class ReplaceVerifiedArea : RootError() {

    class OutOfServiceAreaError : ReplaceVerifiedArea() {
        override val code: Int = 40012
    }
    class InvalidVerifiedArea : ReplaceVerifiedArea() {
        override val code: Int = 40054
    }
    class PeriodRestrictedDeleteError : ReplaceVerifiedArea() {
        override val code: Int = 40055
    }
    class MultiLocationReplaceError : ReplaceVerifiedArea() {
        override val code: Int = 40056
    }
    class VerifiedAreaNotFound : ReplaceVerifiedArea() {
        override val code: Int = 40404
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            OutOfServiceAreaError(),
            InvalidVerifiedArea(),
            PeriodRestrictedDeleteError(),
            MultiLocationReplaceError(),
            VerifiedAreaNotFound()
        )
    }
}