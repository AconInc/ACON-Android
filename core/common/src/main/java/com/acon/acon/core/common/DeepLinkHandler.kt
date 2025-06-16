package com.acon.acon.core.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DeepLinkHandler {
    private val _spotIdFlow = MutableSharedFlow<Long>(extraBufferCapacity = 1)
    val spotIdFlow = _spotIdFlow.asSharedFlow()

    fun handleDeepLink(metadata: Map<String, Any?>) {
        try {
            // spotId 존재 여부 확인
            if (metadata.containsKey("spotId")) {
                val spotIdValue = metadata["spotId"]?.toString()?.toLongOrNull()

                if (spotIdValue != null) {
                    _spotIdFlow.tryEmit(spotIdValue)
                } else {
                    //Timber.w("Invalid spotId format: ${metadata["spotId"]}")
                }
            } else {
                //Timber.w("No spotId found in deep link metadata: $metadata")
            }
        } catch (e: Exception) {
            //Timber.e(e, "Error handling deep link")
        }
    }
}