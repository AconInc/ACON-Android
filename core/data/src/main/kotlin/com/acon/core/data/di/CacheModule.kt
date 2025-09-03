package com.acon.core.data.di

import com.acon.acon.core.common.IODispatcher
import com.acon.core.data.cache.ProfileInfoCacheLegacy
import com.acon.core.data.datasource.remote.ProfileRemoteDataSourceLegacy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun providesProfileInfoCache(
        @IODispatcher scope: CoroutineScope,
        profileRemoteDataSourceLegacy: ProfileRemoteDataSourceLegacy
    ) = ProfileInfoCacheLegacy(scope, profileRemoteDataSourceLegacy)
}