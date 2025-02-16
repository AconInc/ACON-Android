package com.acon.data.di

import com.acon.data.repository.AreaVerificationRepositoryImpl
import com.acon.data.repository.AuthRepositoryImpl
import com.acon.data.repository.MapRepositoryImpl
import com.acon.data.repository.OnboardingRepositoryImpl
import com.acon.data.repository.ProfileRepositoryImpl
import com.acon.data.repository.SpotRepositoryImpl
import com.acon.data.repository.UploadRepositoryImpl
import com.acon.domain.repository.AreaVerificationRepository
import com.acon.data.repository.TokenRepositoryImpl
import com.acon.domain.repository.AuthRepository
import com.acon.domain.repository.MapRepository
import com.acon.domain.repository.OnboardingRepository
import com.acon.domain.repository.ProfileRepository
import com.acon.domain.repository.SpotRepository
import com.acon.domain.repository.TokenRepository
import com.acon.domain.repository.UploadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Singleton
    @Binds
    abstract fun bindsSpotRepository(
        impl: SpotRepositoryImpl
    ): SpotRepository

    @Singleton
    @Binds
    abstract fun bindsTokenLocalRepository(
        impl: TokenRepositoryImpl
    ): TokenRepository

    @Singleton
    @Binds
    abstract fun bindsOnboardingRepository(
        impl: OnboardingRepositoryImpl
    ): OnboardingRepository

    @Singleton
    @Binds
    abstract fun bindsUploadRepository(
        impl: UploadRepositoryImpl
    ): UploadRepository

    @Singleton
    @Binds
    abstract fun bindsAreaVerificationRepository(
        impl: AreaVerificationRepositoryImpl
    ): AreaVerificationRepository

    @Singleton
    @Binds
    abstract fun bindsProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    @Singleton
    @Binds
    abstract fun bindsMapRepository(
        impl: MapRepositoryImpl
    ): MapRepository
}