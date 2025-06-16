package com.acon.acon.core.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DeepLinkHandler {
    private val _spotIdFlow = MutableSharedFlow<Long>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val spotIdFlow = _spotIdFlow.asSharedFlow()

    fun handleDeepLink(metadata: Map<String, Any?>) {
        if (metadata.containsKey("spotId")) {
            val spotIdValue = metadata["spotId"]?.toString()?.toLongOrNull()

            if (spotIdValue != null) {
                _spotIdFlow.tryEmit(spotIdValue)
            }
        }
    }

    fun clear() {
        _spotIdFlow.tryEmit(-1L)
    }
}