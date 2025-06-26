package com.acon.core.navigation.utils

import androidx.navigation.NavHostController

/**
 * route로 이동하며 이전 화면들을 모두 제거
 */
fun<T : Any> NavHostController.navigateAndClear(route: T) {
    navigate(route = route) {
        popUpTo(graph.id) {
            inclusive = true
        }
    }
}