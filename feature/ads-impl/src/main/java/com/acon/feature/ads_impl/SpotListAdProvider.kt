package com.acon.feature.ads_impl

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.acon.core.ads_api.AdProvider
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.effect.imageGradientLayer
import com.acon.acon.core.designsystem.theme.AconTheme
import com.google.android.gms.ads.nativead.NativeAdView

class SpotListAdProvider : AdProvider {

    @Composable
    override fun NativeAd(modifier: Modifier) {
        SpotListNativeAd(modifier)
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun SpotListNativeAd(modifier: Modifier) {
    val context = LocalContext.current
    var adUiState by remember { mutableStateOf<AdUiState>(AdUiState.Loading) }

    DisposableEffect(Unit) {
        val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { ad ->
                adUiState = AdUiState.Success(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adUiState = AdUiState.LoadFailed
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

        onDispose {
            (adUiState as? AdUiState.Success)?.nativeAd?.destroy()
        }
    }

    when(adUiState) {
        is AdUiState.Success -> {
            val ad = (adUiState as AdUiState.Success).nativeAd
            AndroidView(
                modifier = modifier,
                factory = {
                    LayoutInflater.from(it)
                        .inflate(com.acon.feature.ads_impl.R.layout.native_ad_layout, null)
                }, update = { view ->
                    val nativeAdView = view as NativeAdView
                    val composeView =
                        view.findViewById<ComposeView>(com.acon.feature.ads_impl.R.id.compose_view)
                    nativeAdView.setNativeAd(ad)

                    composeView.setContent {
                        AconTheme {
                            Column(Modifier.imageGradientLayer().padding(20.dp)) {
                                Text(
                                    text = ad.headline.orEmpty()
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .background(color = AconTheme.color.GlassWhiteDefault)
                                        .padding(vertical = 9.dp, horizontal = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.advertisement),
                                        style = AconTheme.typography.Body1,
                                        color = AconTheme.color.White,
                                        fontWeight = FontWeight.W400
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                                Text(
                                    text = ad.body.orEmpty(),
                                    style = AconTheme.typography.Body1,
                                    color = AconTheme.color.White,
                                    fontWeight = FontWeight.W400
                                )
                                ad.callToAction?.let {
                                    AconFilledButton(
                                        modifier = Modifier.align(Alignment.End),
                                        onClick = {},
                                        contentPadding = PaddingValues(
                                            horizontal = 23.dp,
                                            vertical = 8.dp
                                        ),
                                    ) {
                                        Text(
                                            text = it,
                                            style = AconTheme.typography.Body1,
                                            fontWeight = FontWeight.SemiBold,
                                            color = AconTheme.color.White,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
        else -> {
            Box(modifier)
        }
    }
}

private sealed interface AdUiState {
    data class Success(val nativeAd: NativeAd) : AdUiState
    data object Loading : AdUiState
    data object LoadFailed : AdUiState
}

@Preview
@Composable
private fun SpotListNativeAdPreview() {
    SpotListNativeAd(Modifier.clip(RoundedCornerShape(20.dp)))
}