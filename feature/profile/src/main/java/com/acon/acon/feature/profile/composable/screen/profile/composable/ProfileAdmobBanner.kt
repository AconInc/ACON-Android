package com.acon.acon.feature.profile.composable.screen.profile.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
internal fun ProfileAdmobBanner(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var adLoaded by remember { mutableStateOf(true) }

    if (adLoaded) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp),
            factory = {
                AdView(context).apply {
                    setAdSize(AdSize.LARGE_BANNER)
                    adUnitId = BuildConfig.SAMPLE_NATIVE_ADMOB_ID
                    adListener = object : com.google.android.gms.ads.AdListener() {
                        override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                            adLoaded = false
                        }
                    }
                    loadAd(AdRequest.Builder().build())
                }
            },
            update = { adView ->
                adView.loadAd(AdRequest.Builder().build())
            }
        )
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    color = AconTheme.color.White,
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}

@Preview
@Composable
private fun ProfileAdmobBannerPreview() {
    ProfileAdmobBanner()
}