package com.acon.acon.domain.error

abstract class RootError: Exception() {
    open val code: Int = 0

    abstract fun createErrorInstances(): Array<RootError>
}