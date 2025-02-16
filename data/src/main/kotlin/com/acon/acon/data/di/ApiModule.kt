package com.acon.acon.data.di

import com.acon.acon.data.remote.AreaVerificationApi
import com.acon.acon.data.remote.AuthApi
import com.acon.acon.data.remote.MapApi
import com.acon.acon.data.remote.OnboardingApi
import com.acon.acon.data.remote.ReissueTokenApi
import com.acon.acon.data.remote.SpotApi
import com.acon.acon.data.remote.UploadApi
import com.acon.acon.core.common.Auth
import com.acon.acon.core.common.Naver
import com.acon.acon.core.common.NoAuth
import com.acon.acon.data.remote.ProfileApi
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