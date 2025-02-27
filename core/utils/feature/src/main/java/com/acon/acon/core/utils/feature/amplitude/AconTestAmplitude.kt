package com.acon.acon.core.utils.feature.amplitude

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.AutocaptureOption
import com.acon.acon.core.utils.feature.BuildConfig
import com.amplitude.android.Configuration
import com.amplitude.common.Logger

object AconTestAmplitude {
    private lateinit var testAmplitude: Amplitude

    fun initialize(
        context: Context,
        apiKey: String = BuildConfig.AMPLITUDE_API_TEST_KEY
    ) {
        if (!AconTestAmplitude::testAmplitude.isInitialized) {
            testAmplitude = Amplitude(
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

    fun trackEvent(eventName: String, properties: Map<String, Any> = emptyMap()) {
        if (AconTestAmplitude::testAmplitude.isInitialized) {
            testAmplitude.track(eventName, properties)
            testAmplitude.logger.logMode = Logger.LogMode.WARN
        }
    }

    fun setUserProperty(userId: String) {
        if (AconTestAmplitude::testAmplitude.isInitialized) {
            testAmplitude.setUserId(userId)
        }
    }
}