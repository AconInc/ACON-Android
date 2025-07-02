package com.acon.acon.domain.error.user

import com.acon.acon.domain.error.RootError

open class PostSignOutError : RootError() {
    class InvalidRefreshToken : PostSignOutError() {
        override val code: Int = 40088
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            InvalidRefreshToken()
        )
    }
}