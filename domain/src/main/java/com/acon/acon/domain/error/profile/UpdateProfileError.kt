package com.acon.acon.domain.error.profile

import com.acon.acon.domain.error.RootError

open class UpdateProfileError : RootError() {

    class AlreadyExistNickname : UpdateProfileError() {
        override val code: Int = -1
    }
    class InvalidNicknameFormat : UpdateProfileError() {
        override val code: Int = -2
    }
    class InvalidBirthDateFormat : UpdateProfileError() {
        override val code: Int = -3
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            AlreadyExistNickname(),
            InvalidNicknameFormat(),
            InvalidBirthDateFormat()
        )
    }
}