package com.acon.acon.domain.usecase

import com.acon.acon.domain.error.profile.ValidateNicknameError
import com.acon.acon.domain.repository.ProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class)
class ValidateNicknameUseCaseTest {

    @MockK
    private lateinit var profileRepository: ProfileRepository

    private lateinit var validateNicknameUseCase: ValidateNicknameUseCase

    @BeforeEach
    fun setUp() {
        validateNicknameUseCase = ValidateNicknameUseCase(profileRepository)
    }

    @Test
    fun `입력이 없을 경우 입력 없음 예외 객체를 Result Wrapping하여 반환한다`() = runTest {
        // Given
        val sampleEmptyNicknameInput = ""

        // When
        val actualException = validateNicknameUseCase(sampleEmptyNicknameInput).exceptionOrNull()

        // Then
        assertIs<ValidateNicknameError.EmptyInput>(actualException)
    }

    @Test
    fun `입력이 14자를 초과했을 경우 길이 초과 예외 객체를 Result Wrapping하여 반환한다`() = runTest {
        // Given
        val sampleNicknameInput = "VerrrrryLoooooooongNickname"

        // When
        val actualException = validateNicknameUseCase(sampleNicknameInput).exceptionOrNull()

        // Then
        assertIs<ValidateNicknameError.InputLengthExceeded>(actualException)
    }

    @Test
    fun `입력에 영어(소문자), 숫자, 밑줄, 마침표가 아닌 것이 포함된 경우 예외 객체를 Result Wrapping하여 반환한다`() = runTest {
        // Given
        val sampleKoreanNicknameInput = "한글닉네임"
        val sampleUppercaseNicknameInput = "Capital"
        val sampleSpaceNicknameInput = "very short"

        // When
        val actualKoreanException = validateNicknameUseCase(sampleKoreanNicknameInput).exceptionOrNull()
        val actualUppercaseException = validateNicknameUseCase(sampleUppercaseNicknameInput).exceptionOrNull()
        val actualSpaceException = validateNicknameUseCase(sampleSpaceNicknameInput).exceptionOrNull()

        // Then
        assertIs<ValidateNicknameError.InvalidFormat>(actualKoreanException)
        assertIs<ValidateNicknameError.InvalidFormat>(actualUppercaseException)
        assertIs<ValidateNicknameError.InvalidFormat>(actualSpaceException)
    }

    @Test
    fun `입력이 유효할 경우 Repository의 유효성 검사 API를 호출하여 그대로 반환한다`() = runTest {
        // Given
        val sampleValidNickname = "thirfir231._."
        val expectedResult = Result.success(Unit)

        coEvery { profileRepository.validateNickname(sampleValidNickname) } returns expectedResult

        // When
        val actualResult = validateNicknameUseCase(sampleValidNickname)

        // Then
        coVerify(exactly = 1) { profileRepository.validateNickname(sampleValidNickname) }
        assertEquals(expectedResult, actualResult)
    }
}