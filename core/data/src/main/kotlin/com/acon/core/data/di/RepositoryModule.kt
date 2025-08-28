package com.acon.core.data.di

import com.acon.core.data.session.SessionHandler
import com.acon.core.data.session.SessionHandlerImpl
import com.acon.core.data.repository.AconAppRepositoryImpl
import com.acon.core.data.repository.MapRepositoryImpl
import com.acon.core.data.repository.MapSearchRepositoryImpl
import com.acon.core.data.repository.OnboardingRepositoryImpl
import com.acon.core.data.repository.ProfileRepositoryImpl
import com.acon.core.data.repository.SpotRepositoryImpl
import com.acon.core.data.repository.TimeRepositoryImpl
import com.acon.core.data.repository.UploadRepositoryImpl
import com.acon.core.data.repository.UserRepositoryImpl
import com.acon.acon.domain.repository.AconAppRepository
import com.acon.acon.domain.repository.MapRepository
import com.acon.acon.domain.repository.MapSearchRepository
import com.acon.acon.domain.repository.OnboardingRepository
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.domain.repository.TimeRepository
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
    abstract fun bindsSessionHandler(
        impl: SessionHandlerImpl
    ): SessionHandler

    @Singleton
    @Binds
    abstract fun bindsSpotRepository(
        impl: SpotRepositoryImpl
    ): SpotRepository

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
    abstract fun bindsMapSearchRepository(
        impl: MapSearchRepositoryImpl
    ): MapSearchRepository

    @Singleton
    @Binds
    abstract fun bindsAconAppRepository(
        impl: AconAppRepositoryImpl
    ): AconAppRepository

    @Singleton
    @Binds
    abstract fun bindsTimeRepository(
        impl: TimeRepositoryImpl
    ): TimeRepository
}