package com.acon.acon.feature.signin.utils

import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.UserRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ComposableEntryPoint {
    fun userRepository(): UserRepository
}

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ComposableActivityEntryPoint {
    fun socialRepository(): SocialRepository
}