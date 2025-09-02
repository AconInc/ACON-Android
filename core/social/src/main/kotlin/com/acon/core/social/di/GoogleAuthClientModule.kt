package com.acon.core.social.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.acon.core.social.BuildConfig
import com.acon.core.social.client.GoogleAuthClient
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object GoogleAuthClientModule {

    private const val SERVER_CLIENT_ID = BuildConfig.GOOGLE_CLIENT_ID

    @ActivityScoped
    @Provides
    fun providesCredentialOption() =
        GetSignInWithGoogleOption.Builder(SERVER_CLIENT_ID).build()

    @ActivityScoped
    @Provides
    fun providesCredentialManager(
        @ActivityContext context: Context
    ) = CredentialManager.create(context)

    @ActivityScoped
    @Provides
    fun providesGoogleAuthClient(
        @ActivityContext context: Context,
        credentialOption: GetSignInWithGoogleOption,
        credentialManager: CredentialManager
    ) = GoogleAuthClient(context, credentialOption, credentialManager)
}

@EntryPoint
@InstallIn(ActivityComponent::class)
interface AuthClientEntryPoint {
    fun googleAuthClient(): GoogleAuthClient
}