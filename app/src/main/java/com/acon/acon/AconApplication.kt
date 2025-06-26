package com.acon.acon

import android.app.Application
import com.acon.core.analytics.amplitude.AconAmplitude
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import io.branch.referral.Branch

@HiltAndroidApp
class AconApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        AconAmplitude.initialize(this)
        Branch.enableLogging()
        Branch.getAutoInstance(this)
    }
}