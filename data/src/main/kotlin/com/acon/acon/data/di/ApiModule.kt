package com.acon.acon.data.di

import com.acon.acon.data.api.remote.AreaVerificationApi
import com.acon.acon.data.api.remote.UserApi
import com.acon.acon.data.api.remote.MapApi
import com.acon.acon.data.api.remote.OnboardingApi
import com.acon.acon.data.api.remote.ReissueTokenApi
import com.acon.acon.data.api.remote.SpotNoAuthApi
import com.acon.acon.data.api.remote.UploadApi
import com.acon.acon.core.common.Auth
import com.acon.acon.core.common.Naver
import com.acon.acon.core.common.NoAuth
import com.acon.acon.data.api.remote.AconAppApi
import com.acon.acon.data.api.remote.ProfileApi
import com.acon.acon.data.api.remote.SpotAuthApi
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
    ): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReissueTokenApi(
       @NoAuth retrofit: Retrofit
    ) : ReissueTokenApi = retrofit.create(ReissueTokenApi::class.java)

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

    @Singleton
    @Provides
    fun providesAconAppApi(
        @NoAuth retrofit: Retrofit
    ): AconAppApi {
        return retrofit.create(AconAppApi::class.java)
    }
}