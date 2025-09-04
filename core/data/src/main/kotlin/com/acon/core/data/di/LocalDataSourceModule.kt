package com.acon.core.data.di

import com.acon.core.data.datasource.local.ProfileLocalDataSource
import com.acon.core.data.datasource.local.ProfileLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsProfileLocalDataSource(
        impl: ProfileLocalDataSourceImpl
    ) : ProfileLocalDataSource
}