package com.acon.acon.di

import android.content.Context
import com.acon.acon.data.authentication.AuthAuthenticator
import com.acon.acon.core.launcher.AppLauncher
import com.acon.acon.data.session.SessionHandler
import com.acon.acon.data.api.remote.ReissueTokenApi
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.launcher.AppLauncherImpl
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
        sessionHandler: SessionHandler,
        reissueTokenApi: ReissueTokenApi,
        appLauncher: AppLauncher
    ): Authenticator = AuthAuthenticator(context, tokenLocalDataSource, sessionHandler, reissueTokenApi, appLauncher)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LauncherModule {

    @Binds
    @Singleton
    abstract fun bindsAppLauncher(impl: AppLauncherImpl): AppLauncher
}