package com.acon.acon.data.repository

import com.acon.acon.data.assertValidErrorMapping
import com.acon.acon.data.createErrorStream
import com.acon.acon.data.createFakeRemoteError
import com.acon.acon.data.datasource.remote.AconAppRemoteDataSource
import com.acon.acon.domain.error.app.FetchShouldUpdateError
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass

@ExtendWith(MockKExtension::class)
class AconAppRepositoryImplTest {

    @RelaxedMockK
    lateinit var aconAppRemoteDataSource: AconAppRemoteDataSource

    @InjectMockKs
    private lateinit var aconAppRepository: AconAppRepositoryImpl

    companion object {
        @JvmStatic
        fun fetchShouldUpdateErrorScenarios() =
            createErrorStream(40045 to FetchShouldUpdateError.InvalidPlatform::class)
    }

    @ParameterizedTest
    @MethodSource("fetchShouldUpdateErrorScenarios")
    fun `앱 업데이트 여부 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<FetchShouldUpdateError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { aconAppRemoteDataSource.fetchShouldUpdateApp(any()) } throws fakeRemoteError

        // When
        val result = aconAppRepository.shouldUpdateApp("Dummy Version")

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }
}