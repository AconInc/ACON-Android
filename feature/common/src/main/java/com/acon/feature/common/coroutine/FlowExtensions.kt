package com.acon.feature.common.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

suspend fun<T> Flow<T>.collectWithLifecycle(
    lifecycle: Lifecycle,
    action: (T) -> Unit
) {
    flowWithLifecycle(lifecycle).collect { value ->
        action(value)
    }
}

suspend fun<T> Flow<T?>.firstNotNull(): T {
    return this.filterNotNull().first()
}