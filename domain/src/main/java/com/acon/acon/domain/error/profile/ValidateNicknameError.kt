package com.acon.acon.domain.error.profile

import com.acon.acon.domain.error.RootError

open class ValidateNicknameError : RootError() {

    class EmptyInput : ValidateNicknameError()
    class InputLengthExceeded : ValidateNicknameError()
    class InvalidFormat : ValidateNicknameError() {
        override val code: Int = -1
    }
    class AlreadyExist : ValidateNicknameError() {
        override val code: Int = -2
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            InvalidFormat(),
            AlreadyExist()
        )
    }
}