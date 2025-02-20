package com.acon.acon.domain.error.profile

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class ValidateNicknameError : RootError() {

    class UnsatisfiedCondition : ValidateNicknameError() {
        override val code: Int = 40051
    }
    class AlreadyUsedNickname : ValidateNicknameError() {
        override val code: Int = 40901
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                UnsatisfiedCondition(),
                AlreadyUsedNickname()
            )
        }
    }
}