package com.acon.acon.domain.error.user

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class PostSignInError : RootError() {
    class InvalidSocialType : PostSignInError() {
        override val code: Int = 40009
    }
    class InvalidIdTokenSignature : PostSignInError() {
        override val code: Int = 40010
    }
    class GooglePublicKeyDownloadFailed : PostSignInError() {
        override val code: Int = 50002
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                InvalidSocialType(),
                InvalidIdTokenSignature(),
                GooglePublicKeyDownloadFailed(),
            )
        }
    }
}