package com.acon.acon.feature.areaverification

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.acon.acon.core.map.ProceedWithLocation
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

@Composable
fun LocationMapScreen(
    onLocationObtained: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    initialLatitude: Double = 0.0,
    initialLongitude: Double = 0.0,
) {
    var naverMap: NaverMap? by remember { mutableStateOf(null) }
    var currentLocation by remember { mutableStateOf<Location?>(null) }

    fun createCustomMarker(map: NaverMap, latitude: Double, longitude: Double) {
        Marker().apply {
            position = LatLng(latitude, longitude)
            width = 80
            height = 100
            icon = OverlayImage.fromResource(R.drawable.ic_mark_48)
            this.map = map
        }
    }

    if (initialLatitude != 0.0 && initialLongitude != 0.0) {
        naverMap?.let { map ->
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                LatLng(initialLatitude, initialLongitude),
                20.0
            )
            map.moveCamera(cameraUpdate)
            createCustomMarker(map, initialLatitude, initialLongitude)
        }
    } else {
        ProceedWithLocation { location ->
            currentLocation = location
            onLocationObtained(location.latitude, location.longitude)
            naverMap?.let { map ->
                val cameraUpdate = CameraUpdate.scrollTo(LatLng(location.latitude, location.longitude))
                map.moveCamera(cameraUpdate)
                createCustomMarker(map, location.latitude, location.longitude)
            }
        }
    }

    Box {
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    getMapAsync { map ->
                        naverMap = map
                        map.uiSettings.apply {
                            isZoomControlEnabled = false
                            isCompassEnabled = true
                        }

                        if (initialLatitude != 0.0 && initialLongitude != 0.0) {
                            val cameraUpdate = CameraUpdate.scrollTo(LatLng(initialLatitude, initialLongitude))
                            map.moveCamera(cameraUpdate)
                            createCustomMarker(map, initialLatitude, initialLongitude)
                        } else {
                            currentLocation?.let { location ->
                                val cameraUpdate = CameraUpdate.scrollTo(LatLng(location.latitude, location.longitude))
                                map.moveCamera(cameraUpdate)
                                createCustomMarker(map, location.latitude, location.longitude)
                            }
                        }
                    }
                }
            },
            modifier = modifier.fillMaxSize()
        )
    }
}
