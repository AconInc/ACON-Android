package com.acon.acon.domain.error.area

import com.acon.acon.domain.error.RootError

open class GetLegalDongError : RootError() {
    class OutOfServiceArea : GetLegalDongError() {
        override val code: Int = 40405
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            OutOfServiceArea()
        )
    }
}