package com.acon.acon.domain.error.user

import com.acon.acon.domain.error.RootError

open class ReissueError : RootError() {
    class InvalidRefreshToken : ReissueError() {
        override val code = 40088
    }

    override fun createErrorInstances(): Array<RootError> {
        return arrayOf(InvalidRefreshToken())
    }
}