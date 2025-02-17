package com.acon.acon.core.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.acon.acon.core.utils.feature.permission.CheckAndRequestLocationPermission
import com.google.android.gms.location.LocationServices

@Composable
@SuppressLint("MissingPermission")
fun ProceedWithLocation(onReady: (Location) -> Unit) {
    val context = LocalContext.current
    val locationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    CheckAndRequestLocationPermission {
        locationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                onReady(it)
            }
        }
    }
}

fun Context.onLocationReady(onReady: (Location) -> Unit) {
    val locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    try {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    onReady(it)
                }
            }
        } else {
            throw SecurityException("Location permission not granted")
        }
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}
