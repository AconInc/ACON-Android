package com.acon.acon.core.utils.feature.constants

object AppURL {
    const val TERM_OF_USE = "https://stripe-shoemaker-907.notion.site/180856d5371b806692f7dcf2bf7088af?pvs=4"
    const val PRIVATE_POLICY = "https://stripe-shoemaker-907.notion.site/180856d5371b80f2b8caf09c3eb69a06?pvs=4"
    const val NAVER_MAP = "market://details?id=com.nhn.android.nmap"

    fun getNaverMapRouteURL(
        currentLat: Double,
        currentLng: Double,
        destinationLat: Double,
        destinationLng: Double,
        destinationName: String
    ): String {
        return "nmap://route/walk?slat=$currentLat&slng=$currentLng&sname=내 위치&" +
                "dlat=$destinationLat&dlng=$destinationLng&dname=$destinationName&" +
                "appname=com.acon.acon"
    }
}