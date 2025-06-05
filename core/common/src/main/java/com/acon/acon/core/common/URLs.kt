package com.acon.acon.core.common

object UrlConstants {
    const val REQUEST_NEW_SPOT = "https://walla.my/survey/ZVXaHzuIVhjQglM1p7fu" // 장소 등록 신청하기
    const val TERM_OF_USE = "https://stripe-shoemaker-907.notion.site/1e1856d5371b8014aaf5eec52d0442f3"
    const val PRIVATE_POLICY = "https://stripe-shoemaker-907.notion.site/1e1856d5371b8017b22bd1a0dad59228"
    const val ERROR_REPORT = "https://walla.my/survey/ekYLYwpJv2d0Eznnijla"
    const val NAVER_MAP = "market://details?id=com.nhn.android.nmap"
    const val NAVER_OPEN_API = "https://naveropenapi.apigw.ntruss.com/"

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