package com.acon.acon.domain.error.profile

import com.acon.acon.domain.error.RootError

open class ValidateNicknameError : RootError() {

    class InvalidNicknameFormat : ValidateNicknameError() {
        override val code: Int = -1
    }
    class AlreadyExistNickname : ValidateNicknameError() {
        override val code: Int = -2
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            InvalidNicknameFormat(),
            AlreadyExistNickname()
        )
    }
}