package com.acon.acon.core.common.utils

import kotlinx.coroutines.delay

suspend fun delay(timeMillis: Int) {
    delay(timeMillis.toLong())
}