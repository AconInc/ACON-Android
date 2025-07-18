package com.acon.acon.domain.error.area

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class PostVerifyAreaError : RootError() {
    class OutOfServiceAreaError : PostVerifyAreaError() {
        override val code: Int = 40012
    }

    class VerifiedAreaLimitViolation : PostVerifyAreaError() {
        override val code: Int = 40405
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                OutOfServiceAreaError(),
                VerifiedAreaLimitViolation()
            )
        }
    }
}