package com.acon.acon.core.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class DeepLinkHandler {
    // spotId를 전달하는 Flow
    private val _spotIdFlow = MutableSharedFlow<Long>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val spotIdFlow = _spotIdFlow.asSharedFlow()

    // 딥링크가 있는지 여부를 나타내는 플래그
    private val _hasDeepLink = MutableStateFlow(false)
    val hasDeepLink = _hasDeepLink.asStateFlow()

    private val _isWarmStart = MutableStateFlow(false)
    val isWarmStart = _isWarmStart.asStateFlow()

    fun handleDeepLink(metadata: Map<String, Any?>,  isWarm: Boolean = false) {
        val spotIdKey = metadata.keys.firstOrNull { it.startsWith("spot/") }
        val spotIdValue = spotIdKey?.let { metadata[it]?.toString()?.toLongOrNull() }

        if (spotIdValue != null) {
            _hasDeepLink.value = true
            _isWarmStart.value = isWarm
            _spotIdFlow.tryEmit(spotIdValue)
        }
    }

    fun clear() {
        _hasDeepLink.value = false
        _isWarmStart.value = false
        _spotIdFlow.tryEmit(-1L)
    }
}