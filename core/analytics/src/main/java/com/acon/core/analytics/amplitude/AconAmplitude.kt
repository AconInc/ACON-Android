package com.acon.core.analytics.amplitude

import android.content.Context
import com.acon.core.analytics.BuildConfig
import com.acon.core.analytics.EventTracker
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.android.events.Identify

object AconAmplitude: EventTracker() {
    private lateinit var amplitude: Amplitude

    override val TEST_API_KEY = BuildConfig.AMPLITUDE_API_TEST_KEY
    override val PRODUCTION_API_KEY = BuildConfig.AMPLITUDE_API_PRODUCTION_KEY

    /**
     * Amplitude에 사용자 ID 설정 (유저 식별을 위함)
     * @param userId 사용자 ID
     */
    override fun setUserId(userId: String) {
        if (!::amplitude.isInitialized) {
            amplitude.setUserId(userId)
        }
    }

    /**
     * Amplitude에 이벤트를 전송
     * @param eventName 이벤트 이름
     * @param properties 이벤트 속성
     *
     * @sample com.acon.core.analytics.sample.sampleAmplitudeTrack
     */
    override fun trackEvent(eventName: String, properties: Map<String, Any>) {
        if (::amplitude.isInitialized) {
            amplitude.track(eventName, properties)
        }
    }

    /**
     * Amplitude에 사용자 속성을 전송
     * @param properties 사용자 속성
     *
     * @sample com.acon.core.analytics.sample.sampleAmplitudeProperties
     */
    override fun setUserProperties(properties: Map<String, String>) {
        if (::amplitude.isInitialized) {
            properties.forEach { (key, value) ->
                amplitude.identify(Identify().set(key, value))
            }
        }
    }

    override fun initialize(
        context: Context,
    ) {
        if (!::amplitude.isInitialized) {
            amplitude = Amplitude(
                Configuration(
                    apiKey = apiKey,
                    context = context,
                    useBatch = true,
                )
            )
        }
    }
}
