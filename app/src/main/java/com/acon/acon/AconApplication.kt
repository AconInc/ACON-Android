package com.acon.acon

import android.app.Application
import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AconApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AconAmplitude.initialize(this)
        AconTestAmplitude.initialize(this)
    }
}