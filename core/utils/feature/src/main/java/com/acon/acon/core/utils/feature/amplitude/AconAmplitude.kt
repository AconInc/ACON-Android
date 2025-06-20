package com.acon.acon.core.utils.feature.amplitude

import android.content.Context
import com.acon.acon.core.utils.feature.BuildConfig
import com.amplitude.android.Amplitude
import com.amplitude.android.AutocaptureOption
import com.amplitude.android.Configuration
import com.amplitude.android.events.Identify

object AconAmplitude {
    private lateinit var amplitude: Amplitude

    fun initialize(
        context: Context,
        apiKey: String = BuildConfig.AMPLITUDE_API_PRODUCTION_KEY
    ) {
        if (!::amplitude.isInitialized) {
            amplitude = Amplitude(
                Configuration(
                    apiKey = apiKey,
                    context = context,
                    autocapture = setOf(
                        AutocaptureOption.SESSIONS
                    ),
                    flushIntervalMillis = 15000,
                    flushQueueSize = 20,
                    flushMaxRetries = 3,
                    useBatch = true,
                    flushEventsOnClose = true,
                    useAppSetIdForDeviceId = false,
                    useAdvertisingIdForDeviceId = false,
                    newDeviceIdPerInstall = false,
                )
            )
        }
    }

    fun setUserId(userId: String) {
        amplitude.setUserId(userId)
    }

    fun trackEvent(eventName: String, properties: Map<String, Any> = emptyMap()) {
//        if (AconAmplitude::amplitude.isInitialized) {
//            amplitude.track(eventName, properties)
//        }
    }

    fun setUserProperty(userId: String) {
        if (AconAmplitude::amplitude.isInitialized) {
            amplitude.setUserId(userId)
        }
    }

    fun setUserProperties(properties: Map<String, String>) {
        if (::amplitude.isInitialized) {
            val identify = Identify()
            properties.forEach { (key, value) ->
                identify.set(key, value)
            }
            amplitude.identify(identify)
        }
    }
}