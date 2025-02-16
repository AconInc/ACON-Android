package com.acon.data.di

import com.acon.core.common.Auth
import com.acon.core.common.Naver
import com.acon.core.common.NoAuth
import com.acon.data.api.remote.AreaVerificationApi
import com.acon.data.api.remote.AuthApi
import com.acon.data.api.remote.MapApi
import com.acon.data.api.remote.OnboardingApi
import com.acon.data.api.remote.ProfileApi
import com.acon.data.api.remote.ReissueTokenApi
import com.acon.data.api.remote.SpotApi
import com.acon.data.api.remote.UploadApi
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
    fun providesAuthApi(
        @Auth retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReissueTokenApi(
       @NoAuth retrofit: Retrofit
    ) : ReissueTokenApi = retrofit.create(ReissueTokenApi::class.java)

    @Singleton
    @Provides
    fun providesSpotApi(
        @Auth retrofit: Retrofit
    ): SpotApi {
        return retrofit.create(SpotApi::class.java)
    }

    @Singleton
    @Provides
    fun providesOnboardingApi(
        @Auth retrofit: Retrofit
    ): OnboardingApi {
        return retrofit.create(OnboardingApi::class.java)
    }

    @Singleton
    @Provides
    fun providesUploadApi(
        @Auth retrofit: Retrofit
    ): UploadApi {
        return retrofit.create(UploadApi::class.java)
    }

    @Singleton
    @Provides
    fun providesAreaVerificationApi(
        @Auth retrofit: Retrofit
    ): AreaVerificationApi {
        return retrofit.create(AreaVerificationApi::class.java)
    }

    @Singleton
    @Provides
    fun providesProfileApi(
        @Auth retrofit: Retrofit
    ): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMapApi(
        @Naver retrofit: Retrofit
    ): MapApi {
        return retrofit.create(MapApi::class.java)
    }
}