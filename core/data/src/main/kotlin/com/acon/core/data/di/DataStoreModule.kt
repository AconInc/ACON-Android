package com.acon.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.acon.core.data.dto.entity.OnboardingPreferencesEntity
import com.acon.core.data.serializer.OnboardingPreferencesSerializer
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
    private val Context.onboardingDataStore: DataStore<OnboardingPreferencesEntity> by dataStore(
        fileName = "onboarding.ds",
        serializer = OnboardingPreferencesSerializer()
    )

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
    fun providesOnboardingDataStore(
        @ApplicationContext context: Context
    ) = context.onboardingDataStore
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AconAppDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TimeDataStore
