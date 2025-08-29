package com.acon.acon.core.navigation.utils

import androidx.navigation.NavHostController

/**
 * route로 이동하며 이전 화면들을 모두 제거
 */
fun <T : Any> NavHostController.navigateAndClear(route: T) {
    navigate(route = route) {
        popUpTo(graph.id) {
            inclusive = true
        }
    }
}

/**
 * Navigation 백스택에 Route가 포함되어 있는지 검사합니다.
 *
 * ```
 * // Example
 * navController.contains<SettingsRoute.UserVerifiedAreas>()
 * ```
 */
inline fun <reified T: Any> NavHostController.contains() : Boolean {
    return try {
        getBackStackEntry<T>()
        true
    } catch (_: IllegalArgumentException) {
        false
    }
}

/**
 * 현재 Entry의 이전 화면이 있는지 검사합니다.
 */
fun NavHostController.hasPreviousBackStackEntry() = previousBackStackEntry != null