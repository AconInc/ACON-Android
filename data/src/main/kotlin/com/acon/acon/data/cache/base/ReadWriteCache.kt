package com.acon.acon.data.cache.base

import com.acon.acon.data.error.runCatchingWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn

abstract class ReadWriteCache<T>(scope: CoroutineScope): ReadOnlyCache<T>(scope) {

    private val _data: MutableStateFlow<Result<T>> by lazy {
        MutableStateFlow(emptyData)
    }

    override val data: StateFlow<Result<T>> by lazy {
        flow {
            _data.emit(runCatchingWith { fetchRemoteData() })

            emitAll(_data)
        }.retry(3) {
            delay(1000)
            it is Exception
        }.stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = emptyData
        )
    }

    open fun updateData(newData: T) {
        _data.value = Result.success(newData)
    }
}