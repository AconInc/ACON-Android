package com.acon.acon.di

import android.content.Context
import com.acon.acon.authentication.AuthAuthenticator
import com.acon.acon.data.SessionManager
import com.acon.acon.data.api.remote.ReissueTokenApi
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.navigator.AppNavigator
import com.acon.acon.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticatorModule {

    @Provides
    @Singleton
    fun providesAuthAuthenticator(
        @ApplicationContext context: Context,
        tokenLocalDataSource: TokenLocalDataSource,
        reissueTokenApi: ReissueTokenApi,
        sessionManager: SessionManager,
        navigator: AppNavigator
    ): Authenticator = AuthAuthenticator(context, tokenLocalDataSource, reissueTokenApi, sessionManager, navigator)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {

    @Binds
    @Singleton
    abstract fun bindsAppNavigator(impl: AppNavigatorImpl): AppNavigator
}