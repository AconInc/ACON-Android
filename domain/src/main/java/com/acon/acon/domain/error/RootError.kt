package com.acon.acon.domain.error

abstract class RootError: Throwable() {
    open val code: Int = 0

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            throw NotImplementedError("createErrorInstances() 메서드가 재정의 되지 않았습니다.")
        }
    }
}

internal interface ErrorFactory {
    fun createErrorInstances(): Array<RootError>
}