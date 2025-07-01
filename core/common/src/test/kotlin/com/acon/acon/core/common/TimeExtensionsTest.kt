package com.acon.acon.core.common

import com.acon.acon.core.common.utils.toHHmm
import com.acon.acon.core.common.utils.toLocalTime
import org.junit.Test
import java.time.LocalTime
import kotlin.test.assertEquals

class TimeExtensionsTest {

    @Test
    fun `String_toLocalTime은 24시간 단위 시-분 문자열을 LocalTime으로 변환한다`() {
        val timeString = "15:34"
        val expectedTime = LocalTime.of(15, 34)
        val actualTime = timeString.toLocalTime()

        assertEquals(expectedTime, actualTime)
    }

    @Test
    fun `LocalTime_toHHmm은 LocalTime을 24시간 단위 시-분 문자열로 변환한다`() {
        val time = LocalTime.of(15, 34)
        val expectedTimeString = "15:34"
        val actualTimeString = time.toHHmm()
    }

    @Test
    fun `LocalTime_toHHmmss은 LocalTime을 24시간 단위 시-분 문자열로 변환한다`() {
        val time = LocalTime.of(15, 34, 21)
        val expectedTimeString = "15:34:21"
        val actualTimeString = time.toHHmm()
    }
}