package com.acon.core.data.datasource.remote

import com.acon.core.data.api.remote.ProfileApi
import com.acon.core.data.dto.request.profile.UpdateProfileRequest
import com.acon.core.data.dto.response.profile.ProfileResponse
import com.acon.core.data.dto.response.profile.SavedSpotResponse
import io.mockk.MockKVerificationScope
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.HttpException
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class ProfileRemoteDataSourceTest {

    @MockK
    private lateinit var profileApi: ProfileApi

    private lateinit var profileRemoteDataSource: ProfileRemoteDataSource

    @BeforeEach
    fun setUp() {
        profileRemoteDataSource = ProfileRemoteDataSourceImpl(profileApi)
    }

    @Test
    fun `getProfile()은 서버로부터 프로필 정보를 가져와 그대로 반환한다`() = runTest {
        // Given
        val expectedProfileResponse = ProfileResponse(
            nickname = "Dummy nickname",
            birthDate = "1999.04.29",
            image = "Dummy profile image url"
        )
        coEvery { profileApi.getProfile() } returns expectedProfileResponse

        // When
        val actualProfileResponse = profileRemoteDataSource.getProfile()

        // Then
        coVerifyOnce { profileApi.getProfile() }
        assertEquals(expectedProfileResponse, actualProfileResponse)
    }

    @Test
    fun `getProfile()은 서버로부터 프로필 정보를 가져올 때 예외가 발생하면 그대로 던진다`() = runTest {
        // Given
        val expectedException = mockk<HttpException>()
        coEvery { profileApi.getProfile() } throws expectedException

        // When
        val actualException = assertThrows<HttpException> {
            profileRemoteDataSource.getProfile()
        }

        // Then
        assertEquals(expectedException, actualException)
    }

    @Test
    fun `updateProfile()은 서버에 새 프로필 정보를 저장한다`() = runTest {
        // Given
        val newProfileRequest = UpdateProfileRequest(
            nickname = "New nickname",
            birthDate = "1999.04.29",
            image = "New image url"
        )

        coEvery { profileApi.updateProfile(any()) } just runs

        // When
        profileRemoteDataSource.updateProfile(newProfileRequest)

        // Then
        coVerifyOnce { profileApi.updateProfile(newProfileRequest) }
    }

    @Test
    fun `updateProfile()은 서버에 새 프로필 정보를 저장할 때 예외가 발생하면 그대로 던진다`() = runTest {
        // Given
        val expectedException = mockk<HttpException>()
        coEvery { profileApi.updateProfile(any()) } throws expectedException

        // When
        val actualException = assertThrows<HttpException> {
            profileRemoteDataSource.updateProfile(mockk())
        }

        // Then
        assertEquals(expectedException, actualException)
    }

    @Test
    fun `validateNickname()은 사용 가능한 닉네임인지를 서버로부터 확인한다`() = runTest {
        // Given
        val sampleNickname = "검사할 닉네임"
        coEvery { profileApi.validateNickname(sampleNickname) } just runs

        // When & Then
        assertDoesNotThrow { profileRemoteDataSource.validateNickname(sampleNickname) }
        coVerifyOnce { profileApi.validateNickname(sampleNickname) }
    }

    @Test
    fun `validateNickname()은 사용 가능한 닉네임인지를 서버로부터 확인할 때 예외가 발생하면 그대로 던진다`() = runTest {
        // Given
        val expectedException = mockk<HttpException>()
        coEvery { profileApi.validateNickname(any()) } throws expectedException

        // When
        val actualException = assertThrows<HttpException> {
            profileRemoteDataSource.validateNickname("Dummy nickname")
        }

        // Then
        assertEquals(expectedException, actualException)
    }

    @Test
    fun `getSavedSpots()는 서버로부터 저장한 장소를 가져와 그대로 반환한다`() = runTest {
        // Given
        val expectedSavedSpotsResponse = listOf(
            SavedSpotResponse(
                spotId = 1,
                spotName = "Spot name1",
                spotThumbnail = "Thumbnail Image Url1"
            ),
            SavedSpotResponse(
                spotId = 2,
                spotName = "Spot name2",
                spotThumbnail = "Thumbnail Image Url2"
            ),
            SavedSpotResponse(
                spotId = 3,
                spotName = "Spot name3",
                spotThumbnail = "Thumbnail Image Url3"
            ),
        )
        coEvery { profileApi.getSavedSpots() } returns expectedSavedSpotsResponse

        // When
        val actualSavedSpotsResponse = profileRemoteDataSource.getSavedSpots()

        // Then
        coVerifyOnce { profileApi.getSavedSpots() }
        assertEquals(expectedSavedSpotsResponse, actualSavedSpotsResponse)
    }

    @Test
    fun `getSavedSpots()는 서버로부터 저장한 장소를 가져올 때 예외가 발생하면 그대로 던진다`() = runTest {
        // Given
        val expectedException = mockk<HttpException>()
        coEvery { profileApi.getSavedSpots() } throws expectedException

        // When
        val actualException = assertThrows<HttpException> {
            profileRemoteDataSource.getSavedSpots()
        }

        // Then
        assertEquals(expectedException, actualException)
    }
}


private fun coVerifyOnce(
    ordering: Ordering = Ordering.UNORDERED,
    inverse: Boolean = false,
    atLeast: Int = 1,
    atMost: Int = Int.MAX_VALUE,
    timeout: Long = 0,
    verifyBlock: suspend MockKVerificationScope.() -> Unit
) {
    coVerify(ordering, inverse, atLeast, atMost, 1, timeout, verifyBlock)
}