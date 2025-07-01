package com.acon.acon.update

import android.app.Application
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.acon.acon.core.common.utils.firstNotNull
import com.acon.acon.domain.repository.AconAppRepository
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await

/**
 * 앱 업데이트 로직 핸들러
 * 1. 강제 업데이트 : 스토어 이동
 * 2. 선택 업데이트 : 인앱 업데이트 수행
 */
interface AppUpdateHandler {

    /**
     * 업데이트 상태 반환
     */
    suspend fun getUpdateState() : UpdateState

    /**
     * 인앱 업데이트 수행
     */
    fun startFlexibleUpdate()
}

class AppUpdateHandlerImpl(
    private val appUpdateManager: AppUpdateManager,
    private val aconAppRepository: AconAppRepository,
    private val appUpdateActivityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
    private val application: Application,
    private val scope: CoroutineScope
) : AppUpdateHandler {

    private val appUpdateInfo = flow {
        emit(appUpdateManager.appUpdateInfo.await())
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    override suspend fun getUpdateState() : UpdateState {
        val shouldUpdateAppDeferred = scope.async {
            val currentAppVersion = try {
                val packageInfo =
                    application.packageManager.getPackageInfo(application.packageName, 0)
                packageInfo.versionName
            } catch (e: Exception) {
                null
            }
            if (currentAppVersion == null)
                return@async false

            aconAppRepository.shouldUpdateApp(currentAppVersion).getOrElse { false }
        }

        appUpdateInfo.firstNotNull().let { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (shouldUpdateAppDeferred.await()) { // 강제 업데이트 (스토어 이동)
                    return UpdateState.FORCE
                } else if (updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) { // 선택적 업데이트 (인앱)
                    return UpdateState.OPTIONAL
                } else if (updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {  // 강제 업데이트 (인앱)
                    // Not used
                }
            }
        }
        return UpdateState.NONE
    }

    override fun startFlexibleUpdate() {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo.value ?: return,
            appUpdateActivityResultLauncher,
            AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE)
        )
    }
}

enum class UpdateState {
    FORCE, OPTIONAL, NONE
}