package com.acon.acon.core.common

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class DeepLinkHandler {
    // Channel → 한 번만 소비되어야 하는 일회성 이벤트 전달을 보장하기 위해 사용
    // CONFLATED : 버퍼가 1, 가장 마지막 데이터만 보장 → 이전 값 덮어씀 (즉, 항상 최신값 한 개만 유지)
    private val _spotIdChannel = Channel<Long>(Channel.CONFLATED)
    val spotIdFlow = _spotIdChannel.receiveAsFlow()

    // 딥링크가 있는지 여부를 나타내는 플래그
    private val _hasDeepLink = MutableStateFlow(false)
    val hasDeepLink = _hasDeepLink.asStateFlow()

    private val _isWarmStart = MutableStateFlow(false)
    val isWarmStart = _isWarmStart.asStateFlow()

    fun handleDeepLink(metadata: Map<String, Any?>, isWarm: Boolean = false) {
        val spotIdKey = metadata.keys.firstOrNull { it == "spotId" }
        val spotIdValue = spotIdKey?.let { metadata[it]?.toString()?.toLongOrNull() }

        if (spotIdValue != null) {
            _hasDeepLink.value = true
            _isWarmStart.value = isWarm
            _spotIdChannel.trySend(spotIdValue)
        }
    }

    fun clear() {
        _hasDeepLink.value = false
        _isWarmStart.value = false
    }
}