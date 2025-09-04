package com.acon.acon.domain.error.profile

import com.acon.acon.domain.error.RootError

open class ValidateNicknameErrorLegacy : RootError() {

    class UnsatisfiedCondition : ValidateNicknameErrorLegacy() {
        override val code: Int = 40051
    }
    class AlreadyUsedNickname : ValidateNicknameErrorLegacy() {
        override val code: Int = 40901
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            UnsatisfiedCondition(),
            AlreadyUsedNickname()
        )
    }
}