package com.acon.core.data.di

import com.acon.core.data.datasource.remote.ProfileRemoteDataSource
import com.acon.core.data.datasource.remote.ProfileRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsProfileRemoteDataSource(
        impl: ProfileRemoteDataSourceImpl
    ) : ProfileRemoteDataSource
}