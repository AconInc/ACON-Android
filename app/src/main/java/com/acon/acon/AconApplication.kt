package com.acon.acon

import android.app.Application
import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import io.branch.referral.Branch

@HiltAndroidApp
class AconApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        AconAmplitude.initialize(this)
        AconTestAmplitude.initialize(this)
        Branch.enableLogging()
        Branch.getAutoInstance(this)
    }
}