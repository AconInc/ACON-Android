package com.acon.core.analytics

import android.content.Context

abstract class EventTracker {

    protected abstract val TEST_API_KEY: String
    protected abstract val PRODUCTION_API_KEY: String

    protected fun getApiKey(): String {
        return if (BuildConfig.DEBUG) {
            TEST_API_KEY
        } else {
            PRODUCTION_API_KEY
        }
    }

    abstract fun initialize(context: Context)
    abstract fun setUserId(userId: String)
    abstract fun trackEvent(eventName: String, properties: Map<String, Any> = emptyMap())
    abstract fun setUserProperties(properties: Map<String, String> = emptyMap())
}