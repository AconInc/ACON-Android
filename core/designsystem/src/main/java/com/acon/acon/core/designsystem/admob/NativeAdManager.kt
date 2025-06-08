package com.acon.acon.core.designsystem.admob

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import timber.log.Timber

class NativeAdManager(
    private val context: Context,
    private val adUnitId: String,
) {
    private var loadedAd: NativeAd? = null

    fun load(onLoaded: (NativeAd?) -> Unit) {
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { nativeAd ->
                loadedAd = nativeAd
                onLoaded(nativeAd)
            }
            .withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Timber.tag(TAG).e("Native ad failed to load: ${error.message}")
                    }
                    override fun onAdLoaded() {
                        Timber.tag(TAG).d("Native ad was loaded.")
                    }

                    override fun onAdImpression() {
                        Timber.tag(TAG).d("Native ad recorded an impression.")
                    }

                    override fun onAdClicked() {
                        Timber.tag(TAG).d("Native ad was clicked.")
                    }
                }
            )
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun destroy() {
        loadedAd?.destroy()
        loadedAd = null
    }

    companion object {
        const val TAG = "admob"
    }
}