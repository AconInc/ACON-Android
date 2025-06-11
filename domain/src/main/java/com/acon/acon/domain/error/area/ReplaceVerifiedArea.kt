package com.acon.acon.domain.error.area

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError
sealed class ReplaceVerifiedArea : RootError() {

    class OutOfServiceAreaError : ReplaceVerifiedArea() {
        override val code: Int = 40012
    }

    class InvalidVerifiedArea : ReplaceVerifiedArea() {
        override val code: Int = 40054
    }

    class VerifiedAreaDeletePeriodRestrictedError : ReplaceVerifiedArea() {
        override val code: Int = 40055
    }

    class VerifiedAreaNotFound : ReplaceVerifiedArea() {
        override val code: Int = 40404
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                OutOfServiceAreaError(),
                InvalidVerifiedArea(),
                VerifiedAreaDeletePeriodRestrictedError(),
                VerifiedAreaNotFound()
            )
        }
    }
}