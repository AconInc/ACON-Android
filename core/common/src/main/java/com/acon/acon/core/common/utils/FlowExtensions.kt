package com.acon.acon.core.common.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

suspend fun<T> Flow<T?>.firstNotNull(): T {
    return this.filterNotNull().first()
}