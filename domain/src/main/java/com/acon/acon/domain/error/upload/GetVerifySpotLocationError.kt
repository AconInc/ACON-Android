package com.acon.acon.domain.error.upload

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class GetVerifySpotLocationError : RootError() {
    class OutOfServiceAreaError : GetVerifySpotLocationError() {
        override val code: Int = 40405
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                OutOfServiceAreaError()
            )
        }
    }
}