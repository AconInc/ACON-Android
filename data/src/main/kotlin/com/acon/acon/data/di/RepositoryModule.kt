package com.acon.acon.data.di

import com.acon.acon.data.repository.AconAppRepositoryImpl
import com.acon.acon.data.repository.MapRepositoryImpl
import com.acon.acon.data.repository.OnboardingRepositoryImpl
import com.acon.acon.data.repository.ProfileRepositoryImpl
import com.acon.acon.data.repository.SpotRepositoryImpl
import com.acon.acon.data.repository.TokenRepositoryImpl
import com.acon.acon.data.repository.UploadRepositoryImpl
import com.acon.acon.data.repository.UserRepositoryImpl
import com.acon.acon.domain.repository.AconAppRepository
import com.acon.acon.domain.repository.MapRepository
import com.acon.acon.domain.repository.OnboardingRepository
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.domain.repository.TokenRepository
import com.acon.acon.domain.repository.UploadRepository
import com.acon.acon.domain.repository.UserRepository
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
    abstract fun bindsUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

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
    abstract fun bindsProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    @Singleton
    @Binds
    abstract fun bindsMapRepository(
        impl: MapRepositoryImpl
    ): MapRepository

    @Singleton
    @Binds
    abstract fun bindsAconAppRepository(
        impl: AconAppRepositoryImpl
    ): AconAppRepository
}