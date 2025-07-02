package com.acon.acon.update

import android.app.Application
import android.content.pm.PackageInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.acon.acon.domain.repository.AconAppRepository
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
class AppUpdateHandlerImplTest {

    @RelaxedMockK
    lateinit var appUpdateManager: AppUpdateManager

    @MockK
    lateinit var aconAppRepository: AconAppRepository

    @RelaxedMockK
    lateinit var appUpdateActivityResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    @RelaxedMockK
    lateinit var application: Application

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val handlerScope = CoroutineScope(testDispatcher + SupervisorJob())

    private val appUpdateHandler: AppUpdateHandler by lazy {
        AppUpdateHandlerImpl(
            appUpdateManager,
            aconAppRepository,
            appUpdateActivityResultLauncher,
            application,
            handlerScope
        )
    }

    @BeforeEach
    fun setUp() {
        val packageInfo = spyk(PackageInfo()).apply { versionName = "1.0.0" }
        every { application.packageManager.getPackageInfo(any<String>(), any<Int>()) } returns packageInfo

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
    }

    @AfterEach
    fun tearDown() {
        handlerScope.cancel()
    }

    companion object {
        /**
         * 강제/업데이트 가능/유연한 업데이트 허용/기대 결과
         */
        @JvmStatic
        fun updateStateScenarios(): Stream<Arguments> = Stream.of(
            Arguments.of(true, UpdateAvailability.UPDATE_AVAILABLE, false, UpdateState.FORCE),      // 강제 업데이트 필요 -> FORCE
            Arguments.of(false, UpdateAvailability.UPDATE_AVAILABLE, true, UpdateState.OPTIONAL),   // 선택 업데이트 가능 -> OPTIONAL
            Arguments.of(true, UpdateAvailability.UPDATE_AVAILABLE, true, UpdateState.FORCE),       // 강제, 선택 업데이트 가능 -> FORCE
            Arguments.of(false, UpdateAvailability.UPDATE_AVAILABLE, false, UpdateState.NONE),      // 업데이트 없음 -> NONE
            Arguments.of(false, UpdateAvailability.UPDATE_NOT_AVAILABLE, false, UpdateState.NONE),  // 업데이트 불가능 -> NONE
            Arguments.of(true, UpdateAvailability.UPDATE_NOT_AVAILABLE, false, UpdateState.NONE)    // 업데이트 불가능 -> NONE
        )
    }

    @ParameterizedTest
    @MethodSource("updateStateScenarios")
    fun `getUpdateState는 다양한 조건에 따라 올바른 상태를 반환한다`(
        shouldForceUpdate: Boolean,
        updateAvailability: Int,
        isFlexibleUpdateAllowed: Boolean,
        expectedState: UpdateState
    ) = testScope.runTest {
        // Given
        val mockAppUpdateInfo = mockk<AppUpdateInfo>()
        val mockTask = mockk<Task<AppUpdateInfo>>()

        coEvery { aconAppRepository.shouldUpdateApp(any()) } returns Result.success(shouldForceUpdate)
        every { mockAppUpdateInfo.updateAvailability() } returns updateAvailability
        every { mockAppUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) } returns isFlexibleUpdateAllowed

        every { mockAppUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) } returns false

        every { appUpdateManager.appUpdateInfo } returns mockTask
        coEvery { mockTask.await() } returns mockAppUpdateInfo

        // When
        val actualUpdateState = appUpdateHandler.getUpdateState()

        // Then
        assertEquals(expectedState, actualUpdateState)
    }
}