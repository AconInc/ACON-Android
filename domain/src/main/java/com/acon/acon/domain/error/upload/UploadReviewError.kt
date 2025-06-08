package com.acon.acon.domain.error.upload

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class UploadReviewError : RootError() {
    class NotEnoughAcorn : UploadReviewError() {
        override val code: Int = 40098
    }

    class NotExistUser : UploadReviewError() {
        override val code: Int = 40402
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                NotEnoughAcorn(),
                NotExistUser()
            )
        }
    }
}