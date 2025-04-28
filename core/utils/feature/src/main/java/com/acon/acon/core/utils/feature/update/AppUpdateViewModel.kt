package com.acon.acon.core.utils.feature.update

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.acon.acon.domain.repository.AconAppRepository
import com.acon.acon.domain.usecase.IsUpdatePostponeTimeExpiredUseCase
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@HiltViewModel(assistedFactory = AppUpdateViewModel.AppUpdateViewModelFactory::class)
class AppUpdateViewModel @AssistedInject constructor(
    @Assisted application: Application,
    private val aconAppRepository: AconAppRepository,
    private val isUpdatePostponeTimeExpiredUseCase: IsUpdatePostponeTimeExpiredUseCase
) : AndroidViewModel(application) {

    private val _updateState = MutableStateFlow(UpdateState.NONE)
    val updateState = _updateState.asStateFlow()

    val appUpdateManager = AppUpdateManagerFactory.create(application)
    val appUpdateInfo = flow {
        val appUpdateInfoDeferred = viewModelScope.async {
            getAppUpdateInfoSuspend(appUpdateManager)
        }
        val shouldUpdateAppDeferred = viewModelScope.async {
            shouldUpdateApp()
        }

        appUpdateInfoDeferred.await()?.let { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {  // 1. 강제 업데이트 (인앱)
                    // Not used
                } else if (shouldUpdateAppDeferred.await()) { // 2. 강제 업데이트 (스토어 이동)
                    _updateState.emit(UpdateState.FORCE)
                    emit(updateInfo)
                } else if (updateInfo.isUpdateAvailable()) { // 3. 선택적 업데이트 (인앱)
                    _updateState.emit(UpdateState.OPTIONAL)
                    emit(updateInfo)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    private suspend fun getAppUpdateInfoSuspend(appUpdateManager: AppUpdateManager): AppUpdateInfo? {
        return suspendCancellableCoroutine { continuation ->
            val task = appUpdateManager.appUpdateInfo
            task.addOnSuccessListener { updateInfo ->
                _updateState.value = UpdateState.NONE
                continuation.resume(updateInfo) {}
            }
            task.addOnFailureListener { exception ->
                continuation.resume(null) {}
            }
        }
    }

    private suspend fun shouldUpdateApp(): Boolean {
        return aconAppRepository.fetchShouldUpdateApp().getOrElse { false }
    }

    private suspend fun AppUpdateInfo.isUpdateAvailable(): Boolean {
        return isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) && isUpdatePostponeTimeExpiredUseCase()
    }

    fun setUpdateState(updateState: UpdateState) {
        _updateState.value = updateState
    }

    @AssistedFactory
    interface AppUpdateViewModelFactory {
        fun create(application: Application): AppUpdateViewModel
    }
}

enum class UpdateState {
    FORCE, OPTIONAL, NONE
}