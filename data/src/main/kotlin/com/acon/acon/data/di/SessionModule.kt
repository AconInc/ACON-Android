package com.acon.acon.data.di

import com.acon.acon.core.common.IODispatcher
import com.acon.acon.data.SessionManager
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Singleton
    @Provides
    fun providesSessionManager(
        tokenLocalDataSource: TokenLocalDataSource,
        @IODispatcher scope: CoroutineScope
    ) = SessionManager(tokenLocalDataSource, scope)
}