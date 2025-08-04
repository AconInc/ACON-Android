package com.acon.acon.domain.error.upload

import com.acon.acon.domain.error.RootError

open class GetUploadPlacePreSignedUrlError : RootError() {
    class NotValidType : GetUploadPlacePreSignedUrlError() {
        override val code: Int = 40045
    }
    class GetFailedUploadPlacePreSignedUrl : GetUploadPlacePreSignedUrlError() {
        override val code: Int = 50005
    }

    override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            NotValidType(),
            GetFailedUploadPlacePreSignedUrl()
        )
    }
}