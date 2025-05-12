package com.acon.feature.common.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.acon.acon.domain.repository.UserRepository
import com.acon.feature.common.di.ComposableEntryPoint
import dagger.hilt.android.EntryPointAccessors

@Composable
fun rememberUserRepository(): UserRepository {
    val context = LocalContext.current
    val entryPoint = EntryPointAccessors.fromApplication(
        context.applicationContext,
        ComposableEntryPoint::class.java
    )

    return remember {
        entryPoint.userRepository()
    }
}