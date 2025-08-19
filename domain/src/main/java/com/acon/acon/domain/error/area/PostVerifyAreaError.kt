package com.acon.acon.domain.error.area

import com.acon.acon.domain.error.RootError

open class PostVerifyAreaError : RootError() {

    class OutOfServiceAreaError : PostVerifyAreaError() {
        override val code: Int = 40012
    }
    class VerifiedAreaLimitViolation : PostVerifyAreaError() {
        override val code: Int = 40405
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            OutOfServiceAreaError(),
            VerifiedAreaLimitViolation()
        )
    }
}