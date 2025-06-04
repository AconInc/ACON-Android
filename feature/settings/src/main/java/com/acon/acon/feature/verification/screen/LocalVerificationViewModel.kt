package com.acon.acon.feature.verification.screen

import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.area.Area
import com.acon.acon.domain.repository.AreaVerificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class LocalVerificationViewModel @Inject constructor(
    private val areaVerificationRepository: AreaVerificationRepository
) : BaseContainerHost<LocalVerificationUiState, LocalVerificationSideEffect>() {

    override val container: Container<LocalVerificationUiState, LocalVerificationSideEffect> =
        container(LocalVerificationUiState.Loading) {
            fetchVerifiedAreaList()
        }

    private fun fetchVerifiedAreaList() = intent {
        areaVerificationRepository.fetchVerifiedAreaList()
            .onSuccess {
                reduce {
                    LocalVerificationUiState.Success(verificationAreaList = it)
                }
            }
            .onFailure {
                LocalVerificationUiState.LoadFailed
            }
    }

    fun deleteVerifiedArea(verifiedAreaId: Long) = intent {
        areaVerificationRepository.deleteVerifiedArea(verifiedAreaId)
            .onSuccess {
                fetchVerifiedAreaList()
            }
            .onFailure {}
    }

    fun onShowEditVerifiedAreaChipDialog(show: Boolean) = intent {
        runOn<LocalVerificationUiState.Success> {
            reduce {
                state.copy(showEditVerifiedAreaChipDialog = show)
            }
        }
    }


    fun onShowDeleteVerifiedAreaChipDialog(show: Boolean, verifiedAreaId: Long) = intent {
        runOn<LocalVerificationUiState.Success> {
            reduce {
                state.copy(
                    showDeleteVerifiedAreaChipDialog = show,
                    selectedAreaId = verifiedAreaId
                )
            }
        }
    }

    fun onNavigateToSettingsScreen() = intent {
        postSideEffect(LocalVerificationSideEffect.NavigateToSettingsScreen)
    }

    fun onNavigateToAreaVerificationAdd() = intent {
        postSideEffect(LocalVerificationSideEffect.NavigateToAreaVerificationToAdd)
    }

    fun onNavigateToAreaVerificationEdit(area: String) = intent {
        postSideEffect(LocalVerificationSideEffect.NavigateToAreaVerificationToEdit(area))
    }

}

sealed interface LocalVerificationUiState {
    data class Success(
        val selectedAreaId: Long? = null,
        val showEditVerifiedAreaChipDialog: Boolean = false,
        val showDeleteVerifiedAreaChipDialog: Boolean = false,
        val verificationAreaList: List<Area>,
    ) : LocalVerificationUiState
    data object Loading : LocalVerificationUiState
    data object LoadFailed: LocalVerificationUiState
}

sealed interface LocalVerificationSideEffect {
    data object NavigateToSettingsScreen : LocalVerificationSideEffect
    data object NavigateToAreaVerificationToAdd : LocalVerificationSideEffect
    data class NavigateToAreaVerificationToEdit(val area: String) : LocalVerificationSideEffect
}