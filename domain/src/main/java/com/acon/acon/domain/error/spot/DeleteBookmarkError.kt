package com.acon.acon.domain.error.spot

import com.acon.acon.domain.error.RootError

open class DeleteBookmarkError : RootError() {
    class SpaceNotFoundError : DeleteBookmarkError() {
        override val code: Int = 40403
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            SpaceNotFoundError()
        )
    }
}