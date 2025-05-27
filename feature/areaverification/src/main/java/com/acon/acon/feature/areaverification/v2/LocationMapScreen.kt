package com.acon.acon.feature.areaverification.v2

import android.location.Location
import android.view.Gravity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.component.toast.AconToastPopup
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.map.BuildConfig
import com.acon.acon.core.map.ProceedWithLocation
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

private const val ZOOM = 20.0
private const val MARKER_WIDTH = 240
private const val MARKER_HEIGHT= 240

@Composable
internal fun LocationMapScreen(
    onLocationObtained: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    initialLatitude: Double = 0.0,
    initialLongitude: Double = 0.0,
    onClickConfirm: () -> Unit = {}
) {
    val context = LocalContext.current
    val density = context.resources.displayMetrics.density

    var naverMap: NaverMap? by remember { mutableStateOf(null) }
    var currentLocation by remember { mutableStateOf<Location?>(null) }

    fun createCustomMarker(map: NaverMap, latitude: Double, longitude: Double) {
        Marker().apply {
            position = LatLng(latitude, longitude)
            width = MARKER_WIDTH
            height = MARKER_HEIGHT
            icon = OverlayImage.fromResource(com.acon.acon.core.designsystem.R.drawable.ic_mark)
            this.map = map
        }
    }

    if (initialLatitude != 0.0 && initialLongitude != 0.0) {
        naverMap?.let { map ->
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                LatLng(initialLatitude, initialLongitude),
                ZOOM
            )
            map.moveCamera(cameraUpdate)
            createCustomMarker(map, initialLatitude, initialLongitude)
        }
    } else {
        ProceedWithLocation { location ->
            currentLocation = location
            onLocationObtained(location.latitude, location.longitude)
            naverMap?.let { map ->
                val cameraUpdate =
                    CameraUpdate.scrollTo(LatLng(location.latitude, location.longitude))
                map.moveCamera(cameraUpdate)
                createCustomMarker(map, location.latitude, location.longitude)
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    getMapAsync { map ->
                        naverMap = map
                        map.customStyleId = BuildConfig.NAVER_NCP_KEY_ID
                        map.uiSettings.apply {
                            isScrollGesturesEnabled = false
                            isZoomGesturesEnabled = false
                            isTiltGesturesEnabled = false
                            isRotateGesturesEnabled = false
                            isZoomControlEnabled = false
                            isCompassEnabled = false
                            isScaleBarEnabled = false
                            logoGravity = Gravity.TOP or Gravity.END
                            setLogoMargin(0, (24 * density).toInt(), (16 * density).toInt(), 0)
                        }

                        if (initialLatitude != 0.0 && initialLongitude != 0.0) {
                            val cameraUpdate =
                                CameraUpdate.scrollTo(LatLng(initialLatitude, initialLongitude))
                            map.moveCamera(cameraUpdate)
                            createCustomMarker(map, initialLatitude, initialLongitude)
                        } else {
                            currentLocation?.let { location ->
                                val cameraUpdate = CameraUpdate.scrollTo(
                                    LatLng(
                                        location.latitude,
                                        location.longitude
                                    )
                                )
                                map.moveCamera(cameraUpdate)
                                createCustomMarker(map, location.latitude, location.longitude)
                            }
                        }
                    }
                }
            }
        )

        AconToastPopup(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 17.dp)
                .padding(horizontal = 16.dp),
            color = AconTheme.color.DimDefault.copy(
                alpha = 0.8f
            ),
            content = {
                Text(
                    text = "인증 지역은 프로필에서 수정 가능합니다.",
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
            }
        )

        AconFilledButton(
            onClick = { onClickConfirm() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AconTheme.color.DimDefault.copy(
                    alpha = 0.8f
                ),
                contentColor = AconTheme.color.White
            ),
            content = {
                Text(
                    text = "인증완료",
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.SemiBold
                )
            }
        )
    }
}