package com.acon.acon.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private val Context.aconAppDataStore: DataStore<Preferences> by preferencesDataStore(name = "acon_app.ds")
    private val Context.timeDataStore: DataStore<Preferences> by preferencesDataStore(name = "time.ds")
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user.ds")

    @Provides
    @Singleton
    @AconAppDataStore
    fun providesAconAppDataStore(
        @ApplicationContext context: Context
    ) = context.aconAppDataStore

    @Provides
    @Singleton
    @TimeDataStore
    fun providesTimeDataStore(
        @ApplicationContext context: Context
    ) = context.timeDataStore

    @Provides
    @Singleton
    @UserDataStore
    fun providesUserDataStore(
        @ApplicationContext context: Context
    ) = context.userDataStore
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AconAppDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TimeDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserDataStore

