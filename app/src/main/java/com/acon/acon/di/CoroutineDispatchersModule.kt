package com.acon.acon.di

import com.acon.acon.core.common.DefaultDispatcher
import com.acon.acon.core.common.IODispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {

    @Singleton
    @Provides
    @IODispatcher
    fun providesIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher() = Dispatchers.Default
}