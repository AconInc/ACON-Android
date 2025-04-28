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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private val Context.aconAppDataStore: DataStore<Preferences> by preferencesDataStore(name = "acon_app.ds")

    @Provides
    @Singleton
    fun providesAconAppDataStore(
        @ApplicationContext context: Context
    ) = context.aconAppDataStore
}