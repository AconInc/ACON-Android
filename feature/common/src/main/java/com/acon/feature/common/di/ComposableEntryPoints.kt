package com.acon.feature.common.di

import com.acon.acon.domain.repository.UserRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ComposableEntryPoint {
    fun userRepository(): UserRepository
}