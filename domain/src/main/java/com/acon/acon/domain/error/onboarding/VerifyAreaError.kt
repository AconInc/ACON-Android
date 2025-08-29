package com.acon.acon.domain.error.onboarding

import com.acon.acon.domain.error.RootError

open class VerifyAreaError : RootError() {

    class OutOfServiceArea : VerifyAreaError() {
        override val code: Int = 40012
    }

    class ExceededVerifiedAreaLimit : VerifyAreaError() {
        override val code: Int = 40032
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            OutOfServiceArea(),
            ExceededVerifiedAreaLimit()
        )
    }
}