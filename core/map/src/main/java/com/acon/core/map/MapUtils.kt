package com.acon.core.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

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
