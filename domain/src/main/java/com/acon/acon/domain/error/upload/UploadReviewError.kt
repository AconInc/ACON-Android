package com.acon.acon.domain.error.upload

import com.acon.acon.domain.error.RootError

open class UploadReviewError : RootError() {
    class NotEnoughAcorn : UploadReviewError() {
        override val code: Int = 40098
    }
    class NotExistUser : UploadReviewError() {
        override val code: Int = 40402
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            NotEnoughAcorn(),
            NotExistUser()
        )
    }
}