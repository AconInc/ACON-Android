package com.acon.acon.domain.error.profile

import com.acon.acon.domain.error.ErrorFactory
import com.acon.acon.domain.error.RootError

sealed class SaveSpotError : RootError() {

    class NotExistSpot : ValidateNicknameError() {
        override val code: Int = 40403
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                NotExistSpot(),
            )
        }
    }
}