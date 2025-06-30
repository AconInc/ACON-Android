package com.acon.acon.domain.error.user

import com.acon.acon.domain.error.RootError

open class PostSignInError : RootError() {
    class InvalidSocialType : PostSignInError() {
        override val code: Int = 40009
    }
    class InvalidIdTokenSignature : PostSignInError() {
        override val code: Int = 40010
    }
    class GooglePublicKeyDownloadFailed : PostSignInError() {
        override val code: Int = 50002
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            InvalidSocialType(),
            InvalidIdTokenSignature(),
            GooglePublicKeyDownloadFailed(),
        )
    }
}