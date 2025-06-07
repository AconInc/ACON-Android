package com.acon.acon.core.utils.feature.constants

object AppURL {
    const val TERM_OF_USE = "https://stripe-shoemaker-907.notion.site/1e1856d5371b8014aaf5eec52d0442f3?pvs=74"
    const val PRIVATE_POLICY = "https://stripe-shoemaker-907.notion.site/1e1856d5371b8017b22bd1a0dad59228?pvs=74"
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