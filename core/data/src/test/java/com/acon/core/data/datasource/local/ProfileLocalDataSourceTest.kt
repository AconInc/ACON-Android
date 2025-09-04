package com.acon.core.data.datasource.local

import app.cash.turbine.test
import com.acon.acon.core.model.model.profile.BirthDateStatus
import com.acon.acon.core.model.model.profile.Profile
import com.acon.acon.core.model.model.profile.ProfileImageStatus
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class ProfileLocalDataSourceTest {

    private lateinit var profileLocalDataSource: ProfileLocalDataSource

    @BeforeEach
    fun setUp() {
        profileLocalDataSource = ProfileLocalDataSourceImpl()
    }

    @Test
    fun `getProfile()은 가장 최근에 캐싱된 값을 반환한다`() = runTest {
        // Given
        val sampleProfile = Profile(
            nickname = "Cached Nickname",
            birthDate = BirthDateStatus.Specified(LocalDate.of(1999,4,29)),
            image = ProfileImageStatus.Custom("Cached Image Url")
        )

        // When
        profileLocalDataSource.getProfile().test {
            awaitItem()
            profileLocalDataSource.cacheProfile(sampleProfile)

            // Then
            assertEquals(sampleProfile, awaitItem())
        }
    }

    @Test
    fun `cacheProfile()은 기존의 캐싱 데이터의 유무에 상관없이, 캐싱 데이터를 새 프로필 값으로 덮어씌운다`() = runTest {
        // Given
        val originalProfile = Profile(
            nickname = "기존에 캐싱된 닉네임",
            birthDate = BirthDateStatus.Specified(LocalDate.of(1999, 4, 29)),
            image = ProfileImageStatus.Default
        )
        val newProfile = Profile(
            nickname = "새롭게 캐싱할 닉네임",
            birthDate = BirthDateStatus.NotSpecified,
            image = ProfileImageStatus.Custom("새롭게 캐싱할 이미지 URL")
        )

        // When & Then
        profileLocalDataSource.getProfile().test {
            awaitItem()

            profileLocalDataSource.cacheProfile(originalProfile)
            assertEquals(originalProfile, awaitItem())

            profileLocalDataSource.cacheProfile(newProfile)
            assertEquals(newProfile, awaitItem())
        }
    }

    @Test
    fun `clearCache()는 캐시를 null로 초기화한다`() = runTest {
        // Given
        val originalProfile = Profile(
            nickname = "기존에 캐싱된 닉네임",
            birthDate = BirthDateStatus.Specified(LocalDate.of(1999, 4, 29)),
            image = ProfileImageStatus.Default
        )

        // When & Then
        profileLocalDataSource.getProfile().test {
            awaitItem()

            profileLocalDataSource.cacheProfile(originalProfile)
            assertEquals(originalProfile, awaitItem())

            profileLocalDataSource.clearCache()
            assertEquals(null, awaitItem())
        }
    }
}