package com.acon.acon.domain.error.profile

import com.acon.acon.domain.error.RootError

open class ValidateNicknameError : RootError() {

    class UnsatisfiedCondition : ValidateNicknameError() {
        override val code: Int = 40051
    }
    class AlreadyUsedNickname : ValidateNicknameError() {
        override val code: Int = 40901
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            UnsatisfiedCondition(),
            AlreadyUsedNickname()
        )
    }
}