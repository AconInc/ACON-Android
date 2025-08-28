package com.acon.core.social.di

import com.acon.acon.core.model.model.user.SocialPlatform
import com.acon.core.social.client.GoogleAuthClient
import com.acon.core.social.client.SocialAuthClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocialClientModule {

    @Provides
    @Singleton
    fun providesGoogleAuthClient() = GoogleAuthClient()

    @Provides
    @Singleton
    fun providesSocialAuthClients(
        googleAuthClient: GoogleAuthClient
    ): Map<SocialPlatform, SocialAuthClient> = mapOf(
        SocialPlatform.GOOGLE to googleAuthClient
    )
}