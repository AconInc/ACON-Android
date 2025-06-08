package com.acon.acon.feature.profile.composable.screen.profile.composable

import android.graphics.Color.WHITE
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.admob.NativeAdAttribution
import com.acon.acon.core.designsystem.admob.NativeAdManager
import com.acon.acon.core.designsystem.animation.skeleton
import com.acon.acon.core.designsystem.component.loading.SkeletonItem
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.BuildConfig
import com.google.android.gms.ads.nativead.AdChoicesView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun DisplayNativeAdView(
    nativeAd: NativeAd?,
    screenHeight: Dp,
    modifier: Modifier = Modifier
) {
    if (nativeAd != null) {
        Box(
            modifier = modifier
                .background(
                    color = AconTheme.color.GlassWhiteDefault,
                    shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .height(maxOf(screenHeight, 125.dp))
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    val paddingDp = 16
                    val paddingPx = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        paddingDp.toFloat(),
                        context.resources.displayMetrics
                    ).toInt()

                    val nativeAdView = NativeAdView(context)
                    val layout = FrameLayout(context).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }

                    val mediaView = MediaView(context).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                    nativeAdView.mediaView = mediaView
                    layout.addView(mediaView)

                    val headlineView = TextView(context).apply {
                        text = nativeAd.headline ?: ""
                        setTextColor(WHITE)
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                        setPadding(paddingPx, paddingPx, 0, 0)
                        letterSpacing = -0.025f
                        val fontSizePx = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 18f, resources.displayMetrics
                        )
                        val lineHeightPx = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 26f, resources.displayMetrics
                        )
                        setLineSpacing(lineHeightPx - fontSizePx, 1f)
                    }

                    nativeAdView.headlineView = headlineView
                    layout.addView(headlineView)

                    val adChoicesView = AdChoicesView(context)
                    nativeAdView.adChoicesView = adChoicesView

                    val adChoicesLayoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.TOP or Gravity.END
                        topMargin = 8
                        rightMargin = 8
                    }
                    layout.addView(adChoicesView, adChoicesLayoutParams)

                    nativeAdView.addView(layout)
                    nativeAdView.setNativeAd(nativeAd)
                    nativeAdView
                },
                update = { view ->
                    view.setNativeAd(nativeAd)
                    (view.headlineView as? TextView)?.text = nativeAd.headline ?: ""
                }
            )
            NativeAdAttribution(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
                    .background(
                        color = AconTheme.color.GlassWhiteDefault,
                        shape = RoundedCornerShape(50)
                    ),
                content = {
                    Text(
                        text = stringResource(R.string.advertisement),
                        style = AconTheme.typography.Body1,
                        color = AconTheme.color.White,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp)
                    )
                }
            )
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(screenHeight)
                .skeleton(
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                SkeletonItem(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(5f),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(Modifier.weight(3f))
                SkeletonItem(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }
}

@Composable
internal fun ProfileNativeAd(
    screenHeight: Dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    val adManager = remember { NativeAdManager(context, BuildConfig.SAMPLE_NATIVE_ADMOB_ID) }

    DisposableEffect(Unit) {
        adManager.load { ad ->
            nativeAd = ad
        }
        onDispose {
            adManager.destroy()
        }
    }

    DisplayNativeAdView(
        nativeAd = nativeAd,
        screenHeight = screenHeight,
        modifier = modifier
    )
}