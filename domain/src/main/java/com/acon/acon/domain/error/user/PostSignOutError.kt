package com.acon.acon.domain.error.user

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class PostSignOutError : RootError() {
    class InvalidRefreshToken : PostSignOutError() {
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