package com.acon.core.data.repository

import com.acon.acon.core.model.model.profile.BirthDateStatus
import com.acon.acon.core.model.model.profile.Profile
import com.acon.acon.core.model.model.profile.ProfileImageStatus
import com.acon.acon.core.model.model.profile.SavedSpot
import com.acon.acon.domain.error.profile.UpdateProfileError
import com.acon.acon.domain.error.profile.ValidateNicknameError
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.core.data.assertValidErrorMapping
import com.acon.core.data.createErrorStream
import com.acon.core.data.createFakeRemoteError
import com.acon.core.data.datasource.local.ProfileLocalDataSource
import com.acon.core.data.datasource.remote.ProfileRemoteDataSource
import com.acon.core.data.dto.response.profile.ProfileResponse
import com.acon.core.data.dto.response.profile.SavedSpotResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import kotlin.reflect.KClass
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
    fun `getProfile()은 서버로부터 프로필 응답을 성공적으로 받아왔을 경우, 모델을 로컬에 캐싱하고 Flow-Result Wrapping하여 반환한다`() = runTest {
        // Given
        val sampleProfileResponse = ProfileResponse(
            nickname = "Sample nickname",
            birthDate = null,
            image = "Sample profile image"
        )
        val sampleProfile = sampleProfileResponse.toProfile()
        val expectedProfileResult = Result.success(sampleProfile)

        coEvery { profileLocalDataSource.cacheProfile(sampleProfile) } just runs
        coEvery { profileLocalDataSource.getProfile() } returns flowOf(null)
        coEvery { profileRemoteDataSource.getProfile() } returns sampleProfileResponse

        // When
        val actualProfileResults = profileRepository.getProfile().toList()

        // Then
        coVerify(exactly = 1) { profileLocalDataSource.cacheProfile(sampleProfile) }

        assertEquals(1, actualProfileResults.size)
        assertEquals(expectedProfileResult, actualProfileResults.first())
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

        coEvery { profileLocalDataSource.getProfile() } returns flowOf(sampleCachedProfile)

        // When
        val actualProfileResults = profileRepository.getProfile().toList()

        // Then
        coVerify(exactly = 0) { profileRemoteDataSource.getProfile() }
        assertEquals(1, actualProfileResults.size)
        assertEquals(expectedProfileResult, actualProfileResults.first())
    }

    @Test
    fun `getProfile()은 로컬에 캐싱된 프로필 불러오기에 실패할 경우 서버로부터 응답을 받아온다`() = runTest {
        // Given
        val sampleProfileResponse = ProfileResponse(
            nickname = "Cached nickname",
            birthDate = null,
            image = null
        )
        val sampleProfile = sampleProfileResponse.toProfile()
        val expectedProfileResult = Result.success(sampleProfile)

        coEvery { profileLocalDataSource.getProfile() } returns flowOf(null)
        coEvery { profileRemoteDataSource.getProfile() } returns sampleProfileResponse
        coEvery { profileLocalDataSource.cacheProfile(sampleProfile) } just runs

        // When
        val actualProfileResults = profileRepository.getProfile().toList()

        // Then
        coVerify(exactly = 1) { profileRemoteDataSource.getProfile() }
        assertEquals(1, actualProfileResults.size)
        assertEquals(expectedProfileResult, actualProfileResults.first())
    }

    @Test
    fun `getProfile()은 로컬에 캐싱된 프로필이 없을 경우 서버 API를 호출한다`() = runTest {
        // Given
        coEvery { profileLocalDataSource.getProfile() } returns flowOf(null)

        // When
        profileRepository.getProfile().collect { }

        // Then
        coVerify(exactly = 1) { profileRemoteDataSource.getProfile() }
    }

    @Test
    fun `getProfile()은 서버로부터 프로필 응답 받기를 실패하면 발생한 예외를 Result wrapping하여 그대로 전파한다`() = runTest {
        // Given
        val fakeException = Exception()
        coEvery { profileLocalDataSource.getProfile() } returns flowOf(null)
        coEvery { profileRemoteDataSource.getProfile() } throws fakeException
        val expectedResult = Result.failure<Profile>(fakeException)

        // When
        val actualResult = profileRepository.getProfile().toList()

        // Then
        assertEquals(1, actualResult.size)
        assertEquals(expectedResult, actualResult.first())
    }

    @Test
    fun `updateProfile()은 서버에 프로필 저장을 성공할 경우, 로컬 캐싱을 업데이트하고 Result(Unit)을 반환한다`() = runTest {
        // Given
        val sampleNewProfile = getSampleNewProfile()
        val expectedResult = Result.success(Unit)
        coEvery { profileRemoteDataSource.updateProfile(any()) } just runs
        coEvery { profileLocalDataSource.cacheProfile(sampleNewProfile) } just runs

        // When
        val actualResult = profileRepository.updateProfile(sampleNewProfile)

        // Then
        coVerify(exactly = 1) { profileLocalDataSource.cacheProfile(sampleNewProfile) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `updateProfile()은 서버에 프로필 저장을 실패할 경우, 로컬 캐싱 값을 업데이트하지 않는다`() = runTest {
        // Given
        val sampleNewProfile = getSampleNewProfile()

        coEvery { profileRemoteDataSource.updateProfile(any()) } throws Exception()

        // When
        profileRepository.updateProfile(sampleNewProfile)

        // Then
        coVerify(exactly = 0) { profileLocalDataSource.cacheProfile(sampleNewProfile) }
    }

    @ParameterizedTest
    @MethodSource("updateProfileErrorScenarios")
    fun `updateProfile()은 서버에 프로필 저장을 실패할 경우 errorCode에 대응하는 예외 객체를 Result Wrapping하여 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<UpdateProfileError>
    ) = runTest {
        // Given
        val sampleNewProfile = getSampleNewProfile()

        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { profileRemoteDataSource.updateProfile(any()) } throws fakeRemoteError

        // When
        val actualResult = profileRepository.updateProfile(sampleNewProfile)

        // Then
        assertValidErrorMapping(actualResult, expectedErrorClass)
    }

    private fun getSampleNewProfile() = Profile(
        nickname = "New nickname",
        birthDate = BirthDateStatus.Specified(LocalDate.of(2000, 1, 1)),
        image = ProfileImageStatus.Default
    )

    @Test
    fun `validateNickname()은 서버로부터 유효성 검사 성공 시 Result(Unit)을 반환한다`() = runTest {
        // Given
        val sampleNickname = "Sample Nickname"
        val expectedResult = Result.success(Unit)
        coEvery { profileRemoteDataSource.validateNickname(any()) } just runs

        // When
        val actualResult = profileRepository.validateNickname(sampleNickname)

        // Then
        coVerify(exactly = 1) { profileRemoteDataSource.validateNickname(sampleNickname) }
        assertEquals(expectedResult, actualResult)
    }

    @ParameterizedTest
    @MethodSource("validateNicknameErrorScenarios")
    fun `validateNickname()은 서버로부터 유효성 검사를 실패할 경우 errorCode에 대응하는 예외 객체를 Result Wrapping하여 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<ValidateNicknameError>
    ) = runTest {
        // Given
        val sampleNickname = "Sample Nickname"
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { profileRemoteDataSource.validateNickname(any()) } throws fakeRemoteError

        // When
        val result = profileRepository.validateNickname(sampleNickname)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @Test
    fun `getSavedSpots()는 서버로부터 저장한 장소 응답받기를 성공하면, 모델로 변환하고 Result Wrapping하여 반환한다`() = runTest {
        // Given
        val sampleSavedSpotsResponse = listOf(
            SavedSpotResponse(1, "Spot1", null),
            SavedSpotResponse(2, "Spot2", "sample url"),
            SavedSpotResponse(3, "Spot3", "sample url")
        )
        coEvery { profileRemoteDataSource.getSavedSpots() } returns sampleSavedSpotsResponse
        val sampleSavedSpots = sampleSavedSpotsResponse.map { it.toSavedSpot() }
        val expectedResult = Result.success(sampleSavedSpots)

        // When
        val actualResult = profileRepository.getSavedSpots()

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `getSavedSpots()는 서버로부터 저장한 장소 응답받기를 실패하면 발생한 예외를 Result wrapping하여 그대로 전파한다`() = runTest {
        // Given
        val fakeException = Exception()
        coEvery { profileRemoteDataSource.getSavedSpots() } throws fakeException
        val expectedResult = Result.failure<List<SavedSpot>>(fakeException)

        // When
        val actualResult = profileRepository.getSavedSpots()

        // Then
        assertEquals(expectedResult, actualResult)
    }

    companion object {
        @JvmStatic
        fun updateProfileErrorScenarios() = createErrorStream(
            -1 to UpdateProfileError.AlreadyExistNickname::class,
            -2 to UpdateProfileError.InvalidNicknameFormat::class,
            -3 to UpdateProfileError.InvalidBirthDateFormat::class
        )

        @JvmStatic
        fun validateNicknameErrorScenarios() = createErrorStream(
            -1 to ValidateNicknameError.InvalidNicknameFormat::class,
            -2 to ValidateNicknameError.AlreadyExistNickname::class
        )
    }
}