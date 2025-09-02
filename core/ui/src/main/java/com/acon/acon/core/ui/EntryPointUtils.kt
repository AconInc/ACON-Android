package com.acon.acon.core.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.acon.acon.core.ui.android.findActivityOrNull
import dagger.hilt.android.EntryPointAccessors

/**
 * Hilt EntryPoint 반환 유틸리티 함수
 *
 * @param T Type of ActivityComponentEntryPoint
 * @return Hilt EntryPoint 객체
 * @throws IllegalArgumentException LocalContext가 Activity가 아닌 경우
 */
@Composable
inline fun <reified T: Any> rememberActivityComponentEntryPoint() : T {
    val context = LocalContext.current
    val activity = remember {
        context.findActivityOrNull()
    }

    requireNotNull(activity) { "Context must be an Activity or a Context that wraps an Activity" }

    return remember {
        EntryPointAccessors.fromActivity(activity, T::class.java)
    }
}

/**
 * Hilt EntryPoint 반환 유틸리티 함수
 *
 * @param T Type of ActivityComponentEntryPoint.
 * @return Hilt EntryPoint 객체
 * @throws IllegalArgumentException Activity Context가 아닌 경우
 */
inline fun <reified T: Any> Context.activityComponentEntryPoint() : T {
    val activity = findActivityOrNull()

    requireNotNull(activity) { "Context must be an Activity or a Context that wraps an Activity" }

    return EntryPointAccessors.fromActivity(activity, T::class.java)
}
