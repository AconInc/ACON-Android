package com.acon.core.data.repository

import com.acon.acon.core.model.model.profile.BirthDateStatus
import com.acon.acon.core.model.model.profile.Profile
import com.acon.acon.core.model.model.profile.ProfileImageStatus
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.core.data.datasource.local.ProfileLocalDataSource
import com.acon.core.data.datasource.remote.ProfileRemoteDataSource
import com.acon.core.data.dto.response.profile.ProfileResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class ProfileRepositoryTest {

    @MockK
    private lateinit var profileRemoteDataSource: ProfileRemoteDataSource

    @MockK
    private lateinit var profileLocalDataSource: ProfileLocalDataSource

    private lateinit var profileRepository: ProfileRepository

    @BeforeEach
    fun setUp() {
        profileRepository = ProfileRepositoryImpl(profileRemoteDataSource, profileLocalDataSource)
    }

    @Test
    fun `getProfile()은 서버로부터 프로필 응답을 성공적으로 받아왔을 경우, Flow-Result Wrapping한 모델을 반환한다`() = runTest {
        // Given
        val sampleProfileResponse = ProfileResponse(
            nickname = "Sample nickname",
            birthDate = "1999.04.29",
            image = "Sample profile image"
        )
        val sampleProfile = sampleProfileResponse.toProfile()
        val expectedProfileResult = Result.success(sampleProfile)

        coEvery { profileLocalDataSource.isCachedProfileExist() } returns false
        coEvery { profileLocalDataSource.cacheProfile(sampleProfile) } just runs
        coEvery { profileRemoteDataSource.getProfile() } returns sampleProfileResponse

        // When
        val actualProfileResults = profileRepository.getProfile().toList()

        // Then
        assertEquals(1, actualProfileResults.size)
        assertEquals(expectedProfileResult, actualProfileResults.first())
    }

    @Test
    fun `getProfile()은 서버로부터 프로필 응답을 성공적으로 받아왔을 경우, 모델을 로컬에 캐싱한다`() = runTest {
        // Given
        val sampleProfileResponse = ProfileResponse(
            nickname = "Sample nickname",
            birthDate = "1999.04.29",
            image = "Sample profile image"
        )
        val sampleProfile = sampleProfileResponse.toProfile()

        coEvery { profileLocalDataSource.isCachedProfileExist() } returns false
        coEvery { profileRemoteDataSource.getProfile() } returns sampleProfileResponse
        coEvery { profileLocalDataSource.cacheProfile(sampleProfile) } just runs

        // When
        profileRepository.getProfile().collect { }

        // Then
        coVerify(exactly = 1) { profileLocalDataSource.cacheProfile(sampleProfile) }
    }

    @Test
    fun `getProfile()은 로컬에 캐싱된 프로필이 있을 경우 서버 API를 호출하지 않고 캐싱 값을 반환한다`() = runTest {
        // Given
        val sampleCachedProfile = Profile(
            nickname = "Cached nickname",
            birthDate = BirthDateStatus.Specified(LocalDate.of(1999, 4, 29)),
            image = ProfileImageStatus.Custom("Cached image url")
        )
        val expectedProfileResult = Result.success(sampleCachedProfile)

        coEvery { profileLocalDataSource.isCachedProfileExist() } returns true
        coEvery { profileLocalDataSource.getProfile() } returns sampleCachedProfile

        // When
        val actualProfileResults = profileRepository.getProfile().toList()

        // Then
        coVerify(exactly = 0) { profileRemoteDataSource.getProfile() }
        assertEquals(1, actualProfileResults.size)
        assertEquals(expectedProfileResult, actualProfileResults.first())
    }

    @Test
    fun `getProfile()은 로컬에 캐싱된 프로필이 없을 경우 서버 API를 호출한다`() = runTest {
        // Given
        coEvery { profileLocalDataSource.isCachedProfileExist() } returns false

        // When
        profileRepository.getProfile().collect { }

        // Then
        coVerify(exactly = 0) { profileLocalDataSource.getProfile() }
        coVerify(exactly = 1) { profileRemoteDataSource.getProfile() }
    }
}