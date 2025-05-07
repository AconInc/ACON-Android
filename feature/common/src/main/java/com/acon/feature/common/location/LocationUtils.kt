package com.acon.feature.common.location

import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * 현재 위치를 반환. 결과가 null일 경우 실패(위치 권한 미허용, GPS OFF 등)
 * @return 현재 위치
 */
@RequiresPermission(
    allOf = [
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION"
    ]
)
suspend fun Context.getLocation(): Location? {
    val locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    return locationProviderClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        null
    ).await()
}

/**
 * 실시간으로 현재 위치를 방출하는 Flow
 * @param intervalMillis 업데이트 간격
 * @return 현재 위치 Flow
 */
@RequiresPermission(
    allOf = [
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION"
    ]
)
fun Context.locationFlow(
    intervalMillis: Long = 3_000L
) = callbackFlow<Location> {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@locationFlow)

    val locationRequest = LocationRequest.Builder(intervalMillis).setPriority(
        Priority.PRIORITY_HIGH_ACCURACY
    ).build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                Timber.d("새 좌표 획득: [${location.latitude}, ${location.longitude}]")
                trySend(location)
            }
        }
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )

    awaitClose {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}