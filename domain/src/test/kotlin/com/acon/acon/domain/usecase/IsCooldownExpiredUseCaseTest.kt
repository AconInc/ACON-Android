package com.acon.acon.domain.usecase

import com.acon.acon.domain.repository.TimeRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class IsCooldownExpiredUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var timeRepository: TimeRepository

    private val isCooldownExpiredUseCase by lazy {
        IsCooldownExpiredUseCase(timeRepository)
    }

    private val cooldown = 24 * 60 * 60L // 24h to seconds

    @Test
    fun `isCooldownExpiredUseCase는 저장된 시간에서 cooldown 이상의 시간이 흘렀을 경우 true를 반환한다`() = runTest {
        // Given
        coEvery { timeRepository.getUserActionTime(any()) } returns Result.success(0L)
        val fakeCurrentTimestamp = 24 * 60 * 60 * 1000 + 1L

        // When
        val useCaseResult = isCooldownExpiredUseCase(mockk(), cooldown, fakeCurrentTimestamp)

        // Then
        assertEquals(true, useCaseResult)
    }

    @Test
    fun `isCooldownExpiredUseCase는 저장된 시간에서 cooldown 이상의 시간이 흐르지 않았을 경우 false를 반환한다`() = runTest {
        // Given
        coEvery { timeRepository.getUserActionTime(any()) } returns Result.success(0L)
        val fakeCurrentTimestamp = 24 * 60 * 60 * 1000 - 1L

        // When
        val useCaseResult = isCooldownExpiredUseCase(mockk(), cooldown, fakeCurrentTimestamp)

        // Then
        assertEquals(false, useCaseResult)
    }
}