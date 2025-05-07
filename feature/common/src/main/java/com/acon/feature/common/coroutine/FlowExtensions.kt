package com.acon.feature.common.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

suspend fun<T> Flow<T>.collectWithLifecycle(
    lifecycle: Lifecycle,
    action: (T) -> Unit
) {
    flowWithLifecycle(lifecycle).collect { value ->
        action(value)
    }
}
