package com.acon.acon.domain.error.user

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class PostLogoutError : RootError() {
    class InvalidRefreshToken : PostLogoutError() {
        override val code: Int = 40088
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                InvalidRefreshToken()
            )
        }
    }
}