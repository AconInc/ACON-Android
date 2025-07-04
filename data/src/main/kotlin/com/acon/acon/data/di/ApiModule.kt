package com.acon.acon.data.di

import com.acon.acon.core.common.Auth
import com.acon.acon.core.common.Naver
import com.acon.acon.core.common.NoAuth
import com.acon.acon.data.api.remote.noauth.AconAppNoAuthApi
import com.acon.acon.data.api.remote.MapApi
import com.acon.acon.data.api.remote.auth.OnboardingAuthApi
import com.acon.acon.data.api.remote.auth.ProfileAuthApi
import com.acon.acon.data.api.remote.auth.SpotAuthApi
import com.acon.acon.data.api.remote.noauth.SpotNoAuthApi
import com.acon.acon.data.api.remote.auth.UploadAuthApi
import com.acon.acon.data.api.remote.auth.UserAuthApi
import com.acon.acon.data.api.remote.noauth.UserNoAuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    @Singleton
    @Provides
    fun providesUserApi(
        @Auth retrofit: Retrofit
    ): UserAuthApi {
        return retrofit.create(UserAuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesUserNoAuthApi(
        @NoAuth retrofit: Retrofit
    ): UserNoAuthApi {
        return retrofit.create(UserNoAuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesSpotNoAuthApi(
        @NoAuth retrofit: Retrofit
    ): SpotNoAuthApi {
        return retrofit.create(SpotNoAuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesSpotAuthApi(
        @Auth retrofit: Retrofit
    ): SpotAuthApi {
        return retrofit.create(SpotAuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesOnboardingApi(
        @Auth retrofit: Retrofit
    ): OnboardingAuthApi {
        return retrofit.create(OnboardingAuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesUploadApi(
        @Auth retrofit: Retrofit
    ): UploadAuthApi {
        return retrofit.create(UploadAuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesProfileApi(
        @Auth retrofit: Retrofit
    ): ProfileAuthApi {
        return retrofit.create(ProfileAuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMapApi(
        @Naver retrofit: Retrofit
    ): MapApi {
        return retrofit.create(MapApi::class.java)
    }

    @Singleton
    @Provides
    fun providesAconAppApi(
        @NoAuth retrofit: Retrofit
    ): AconAppNoAuthApi {
        return retrofit.create(AconAppNoAuthApi::class.java)
    }
}