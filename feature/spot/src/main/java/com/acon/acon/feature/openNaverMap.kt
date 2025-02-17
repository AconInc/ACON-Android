package com.acon.acon.feature

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import com.acon.acon.core.utils.feature.constants.AppURL

internal fun openNaverMap(
    context: Context,
    location: Location,
    destinationLat: Double,
    destinationLng: Double,
    destinationName: String
) {
    val url = AppURL.getNaverMapRouteURL(
        location.latitude, location.longitude,
        destinationLat, destinationLng, destinationName
    )

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
    }

    val packageManager = context.packageManager
    val resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

    if (resolveInfoList.isEmpty()) {
        val playStoreIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(AppURL.NAVER_MAP)
        )
        context.startActivity(playStoreIntent)
    } else {
        context.startActivity(intent)
    }
}