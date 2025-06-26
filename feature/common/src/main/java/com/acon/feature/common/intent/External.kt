package com.acon.feature.common.intent

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import com.acon.core.type.TransportMode

/**
 * Acon 플레이스토어로 이동
 */
fun Context.launchPlayStore() {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("market://details?id=${packageName}")
        setPackage("com.android.vending")
    }
    startActivity(intent)
}

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

fun Context.openNaverMapNavigationWithMode(
    start: Location,
    destination: Location,
    destinationName: String,
    isPublic: Boolean,
    transportMode: com.acon.core.type.TransportMode? = null,
) {
    val mode = when {
        isPublic -> "public"
        transportMode == com.acon.core.type.TransportMode.WALKING -> "walk"
        transportMode == com.acon.core.type.TransportMode.BIKING -> "bicycle"
        else -> "public"
    }
    val uri = Uri.parse(
        "nmap://route/$mode?" +
                "dlat=${destination.latitude}&dlng=${destination.longitude}&" +
                "dname=$destinationName&" +
                "appname=$packageName"
    )

    if (isPackageInstalled(this, NAVER_MAP_PACKAGE_NAME)) {
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage(NAVER_MAP_PACKAGE_NAME)
        }
        startActivity(intent)
    } else {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${NAVER_MAP_PACKAGE_NAME}")
            )
        )
    }
}

private const val NAVER_MAP_PACKAGE_NAME = "com.nhn.android.nmap"
private const val KAKAO_MAP_PACKAGE_NAME = "net.daum.android.map"
private const val GOOGLE_MAP_PACKAGE_NAME = "com.google.android.apps.maps"

abstract class NavigationAppHandler {

    protected abstract val packageName: String
    protected abstract val uri: Uri

    fun startNavigationApp(context: Context) {
        if (isPackageInstalled(context, packageName)) {
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage(packageName)
            }
            context.startActivity(intent)
        } else {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${packageName}")
                )
            )
        }
    }
}

class NaverNavigationAppHandler(
    destination: Location,
    dName: String,
    mode: com.acon.core.type.TransportMode?
): NavigationAppHandler() {

    private val transitBy = when(mode) {
        com.acon.core.type.TransportMode.WALKING -> "walk"
        com.acon.core.type.TransportMode.BIKING -> "bicycle"
        else -> "public"
    }

    override val packageName: String = "com.nhn.android.nmap"
    override val uri: Uri = Uri.parse(
        "nmap://route/$transitBy?" +
                "dlat=${destination.latitude}&dlng=${destination.longitude}&" +
                "dname=$dName&" +
                "appname=$packageName"
    )
}

class KakaoNavigationAppHandler(
    start: Location,
    destination: Location,
    mode: com.acon.core.type.TransportMode?
): NavigationAppHandler() {

    private val transitBy = when(mode) {
        com.acon.core.type.TransportMode.WALKING -> "FOOT"
        com.acon.core.type.TransportMode.BIKING -> "FOOT"
        else -> "PUBLICTRANSIT"
    }

    override val packageName: String = "net.daum.android.map"
    override val uri: Uri = Uri.parse(
        "kakaomap://route?sp=${start.latitude},${start.longitude}&ep=${destination.latitude},${destination.longitude}&by=${transitBy}"
    )

}

class GoogleNavigationAppHandler(
    destination: Location,
    mode: com.acon.core.type.TransportMode?
): NavigationAppHandler() {

    private val travelMode = when(mode) {
        com.acon.core.type.TransportMode.WALKING -> "walking"
        com.acon.core.type.TransportMode.BIKING -> "bicycling"
        else -> "driving"
    }

    override val packageName: String = "com.google.android.apps.maps"
    override val uri: Uri = Uri.parse(
        "https://www.google.com/maps/dir/?api=1&destination=${destination.latitude},${destination.longitude}&travelmode=${travelMode}"
    )
}