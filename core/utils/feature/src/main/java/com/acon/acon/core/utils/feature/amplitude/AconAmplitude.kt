package com.acon.acon.core.utils.feature.amplitude

import android.content.Context
import android.util.Log
import com.acon.acon.core.utils.feature.BuildConfig
import com.amplitude.android.Amplitude
import com.amplitude.android.AutocaptureOption
import com.amplitude.android.Configuration
import com.amplitude.android.events.Identify
import com.amplitude.common.Logger

object AconAmplitude {
    private lateinit var amplitude: Amplitude
    private const val TAG = "AconAmplitude"

    fun initialize(
        context: Context,
        apiKey: String = BuildConfig.AMPLITUDE_API_PRODUCTION_KEY
    ) {
        if (!::amplitude.isInitialized) {
            amplitude = Amplitude(
                Configuration(
                    apiKey = apiKey,
                    context = context,
                    useAppSetIdForDeviceId = false,
                    autocapture = setOf(
                        AutocaptureOption.SESSIONS,
                        AutocaptureOption.APP_LIFECYCLES
                    ),
                )
            )
        }
    }

    fun trackEvent(eventName: String, properties: Map<String, Any> = emptyMap()) {
        if (AconAmplitude::amplitude.isInitialized) {
            amplitude.track(eventName, properties)
            amplitude.logger.logMode = Logger.LogMode.WARN
            Log.d(TAG, "eventName: $eventName, properties : $properties")
        }
    }

    fun setUserProperty(userId: String) {
        if (AconAmplitude::amplitude.isInitialized) {
            amplitude.setUserId(userId)
            Log.d(TAG, "userId: $userId")
        }
    }

    fun setUserProperties(properties: Map<String, String>) {
        if (::amplitude.isInitialized) {
            val identify = Identify()
            properties.forEach { (key, value) ->
                identify.set(key, value)
            }
            amplitude.identify(identify)
            Log.d(TAG, "User Properties Set: $properties")
        } else {
            Log.e(TAG, "Amplitude가 초기화되지 않음")
        }
    }
}