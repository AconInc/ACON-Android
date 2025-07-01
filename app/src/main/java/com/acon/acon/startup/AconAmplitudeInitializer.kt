package com.acon.acon.startup

import android.content.Context
import androidx.startup.Initializer
import com.acon.acon.core.analytics.amplitude.AconAmplitude

class AconAmplitudeInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        AconAmplitude.initialize(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}