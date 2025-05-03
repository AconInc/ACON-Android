package com.acon.core.analytics

import android.content.Context
import com.acon.core.analytics.amplitude.AconAmplitude

object EventLogger {

    private val tracker: EventTracker = AconAmplitude()

    /**
     * 이벤트 추적
     * @param eventName 이벤트 이름
     * @param properties 이벤트 속성
     *
     * @sample com.acon.core.analytics.sample.sampleTrack
     */
    fun trackEvent(eventName: String, properties: Map<String, Any> = emptyMap()) {
        tracker.trackEvent(eventName, properties)
    }

    /**
     * 사용자 ID 설정
     * @param userId 사용자 ID
     */
    fun setUserId(userId: String) {
        tracker.setUserId(userId)
    }

    /**
     * 사용자 속성 설정
     * @param properties 사용자 속성
     */
    fun setUserProperties(properties: Map<String, String> = emptyMap()) {
        tracker.setUserProperties(properties)
    }

    /**
     * 초기화
     * @param context Context
     */
    fun initialize(context: Context) {
        tracker.initialize(context)
    }
}