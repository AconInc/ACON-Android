package com.acon.acon.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class IsDistanceExceededUseCaseTest {

    private val isDistanceExceededUseCase = IsDistanceExceededUseCase()

    private val thresholdKm = 0.05 // 50m

    @Test
    fun `getDistance는 두 좌표 간의 거리를 하버사인 공식에 따라 km 단위로 올바르게 계산한다`() {
        val lat1 = 37.566500
        val lon1 = 126.978000
        val lat2 = 37.551200
        val lon2 = 126.988200

        val actualDist = isDistanceExceededUseCase.getDistance(lat1, lon1, lat2, lon2)

        val expectedDist = 1.9264
        val delta = 0.01

        assertEquals(expectedDist, actualDist, delta)
    }

    @Test
    fun `거리를 초과하면 invoke()는 true를 반환한다`() {
        val lat1 = 37.566500
        val lon1 = 126.978000

        assertTrue(isDistanceExceededUseCase(lat1, lon1, 37.566951, 126.978000, thresholdKm))
        assertTrue(isDistanceExceededUseCase(lat1, lon1, 37.567500, 126.978000, thresholdKm))
        assertTrue(isDistanceExceededUseCase(lat1, lon1, 37.566950, 126.978500, thresholdKm))
    }

    @Test
    fun `거리를 초과하지 않으면 invoke()는 false를 반환한다`() {
        val lat1 = 37.566500
        val lon1 = 126.978000

        assertFalse(isDistanceExceededUseCase(lat1, lon1, 37.566500, 126.978000, thresholdKm))
        assertFalse(isDistanceExceededUseCase(lat1, lon1, 37.566600, 126.978000, thresholdKm))
        assertFalse(isDistanceExceededUseCase(lat1, lon1, 37.566949, 126.978000, thresholdKm))
    }
}
