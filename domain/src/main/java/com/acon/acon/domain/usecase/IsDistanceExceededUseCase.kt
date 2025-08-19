package com.acon.acon.domain.usecase

import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.sqrt

class IsDistanceExceededUseCase @Inject constructor() {

    /**
     * 두 거리가 thresholdKm 이상 떨어져 있는지 여부
     * @param latitude1 위도1
     * @param longitude1 경도1
     * @param latitude2 위도2
     * @param longitude2 경도2
     * @param thresholdKm 기준 거리(킬로미터)
     */
    operator fun invoke(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double,
        thresholdKm: Double = 1.3
    ): Boolean {
        return getDistance(latitude1, longitude1, latitude2, longitude2) >= thresholdKm
    }

    /**
     * 두 좌표 간 거리 계산 (km)
     * 하버사인 공식 사용
     */
    internal fun getDistance(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double,
    ): Double {
        val R = 6371.0 // 지구 반지름 (킬로미터)

        val dLat = Math.toRadians(latitude2 - latitude1)
        val dLon = Math.toRadians(longitude2 - longitude1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(latitude1)) * cos(Math.toRadians(latitude2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
    }
}