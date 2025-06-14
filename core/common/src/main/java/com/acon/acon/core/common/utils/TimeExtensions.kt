package com.acon.acon.core.common.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val HHmmFormatter by lazy {
    DateTimeFormatter.ofPattern("HH:mm")
}

/**
 * HH:mm을 LocalTime으로 변환.
 * 파싱 실패 시 null 반환
 * ```
 * "23:59".toLocalTime()    // == LocalTime.of(23, 59)
 * ```
 */
fun String?.toLocalTime(): LocalTime? {
    return try {
        this?.let { LocalTime.parse(it, HHmmFormatter) }
    } catch (e: DateTimeParseException) {
        null
    }
}

/**
 * LocalTime을 HH:mm 형식으로 변환
 */
fun LocalTime.toHHmm(): String {
    return format(HHmmFormatter)
}