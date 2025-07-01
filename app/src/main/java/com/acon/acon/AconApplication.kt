package com.acon.acon

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import io.branch.referral.Branch

@HiltAndroidApp
class AconApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        Branch.enableLogging()
        Branch.getAutoInstance(this)
    }
}