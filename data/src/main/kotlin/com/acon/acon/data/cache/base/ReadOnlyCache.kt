package com.acon.acon.data.cache.base

import com.acon.acon.data.error.runCatchingWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn

abstract class ReadOnlyCache<T>(scope: CoroutineScope) {
    abstract val emptyData: Result<T>

    open val data: StateFlow<Result<T>> by lazy {
        flow {
            emit(runCatchingWith { fetchRemoteData() })
        }.retry(3) {
            delay(1000)
            it is Exception
        }.stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = emptyData
        )
    }

    abstract suspend fun fetchRemoteData(): T
}