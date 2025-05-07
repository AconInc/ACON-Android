package com.acon.feature.common.intent

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri

fun Context.openMapNavigation(
    start: Location,
    destination: Location
) {
    when {
        isPackageInstalled(this, NAVER_MAP_PACKAGE_NAME) -> openNaverMapNavigation(start, destination)
        isPackageInstalled(this, KAKAO_MAP_PACKAGE_NAME) -> openKakaoMapNavigation(start, destination)
        isPackageInstalled(this, GOOGLE_MAP_PACKAGE_NAME) -> openGoogleMapNavigation(start, destination)
        else -> {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${NAVER_MAP_PACKAGE_NAME}")
                )
            )
        }
    }
}

private fun isPackageInstalled(context: Context, packageName: String): Boolean {
    return try {
        context.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

private fun Context.openNaverMapNavigation(
    start: Location,
    destination: Location
) {
    val uri = Uri.parse("nmap://route/public?dlat=${destination.latitude}&dlng=${destination.longitude}&appname=${packageName}}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage(NAVER_MAP_PACKAGE_NAME)
    startActivity(intent)
}

private fun Context.openKakaoMapNavigation(
    start: Location,
    destination: Location
) {
    val uri = Uri.parse("kakaomap://route?sp=${start.latitude},${start.longitude}&ep=${destination.latitude},${destination.longitude}&by=FOOT")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage(KAKAO_MAP_PACKAGE_NAME)
    startActivity(intent)
}

private fun Context.openGoogleMapNavigation(
    start: Location,
    destination: Location
) {
    val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${destination.latitude},${destination.longitude}&travelmode=walking")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage(GOOGLE_MAP_PACKAGE_NAME)
    startActivity(intent)
}

private const val NAVER_MAP_PACKAGE_NAME = "com.nhn.android.nmap"
private const val KAKAO_MAP_PACKAGE_NAME = "net.daum.android.map"
private const val GOOGLE_MAP_PACKAGE_NAME = "com.google.android.apps.maps"