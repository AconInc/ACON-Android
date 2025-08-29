package com.acon.core.map.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.acon.acon.core.designsystem.R
import com.acon.core.map.BuildConfig
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

private const val MARKER_WIDTH = 240
private const val MARKER_HEIGHT = 240

@Composable
fun NaverMapView(
    modifier: Modifier = Modifier,
    latitude: Double,
    longitude: Double,
) {
    val context = LocalContext.current
    val density = context.resources.displayMetrics.density

    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                getMapAsync { map ->
                    map.customStyleId = BuildConfig.NAVER_NCP_KEY_ID
                    map.uiSettings.apply {
                        isScrollGesturesEnabled = false
                        isZoomGesturesEnabled = false
                        isTiltGesturesEnabled = false
                        isRotateGesturesEnabled = false
                        isZoomControlEnabled = false
                        isCompassEnabled = false
                        isScaleBarEnabled = false
                        setLogoMargin((20 * density).toInt(), 0, 0, (100 * density).toInt())
                    }

                    val cameraUpdate =
                        CameraUpdate.scrollTo(LatLng(latitude, longitude))
                    map.moveCamera(cameraUpdate)
                    createCustomMarker(map, latitude, longitude)
                }
            }
        }
    )
}

private fun createCustomMarker(map: NaverMap, latitude: Double, longitude: Double) {
    Marker().apply {
        position = LatLng(latitude, longitude)
        width = MARKER_WIDTH
        height = MARKER_HEIGHT
        icon = OverlayImage.fromResource(R.drawable.ic_mark)
        this.map = map
    }
}
