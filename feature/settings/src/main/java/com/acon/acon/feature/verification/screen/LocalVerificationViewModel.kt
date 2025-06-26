package com.acon.acon.feature.verification.screen

import com.acon.acon.domain.error.area.DeleteVerifiedAreaError
import com.acon.core.model.area.Area
import com.acon.acon.domain.repository.UserRepository
import com.acon.core.ui.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class LocalVerificationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseContainerHost<LocalVerificationUiState, LocalVerificationSideEffect>() {

    override val container: Container<LocalVerificationUiState, LocalVerificationSideEffect> =
        container(LocalVerificationUiState.Loading) {
            fetchVerifiedAreaList()
        }

    private fun fetchVerifiedAreaList() = intent {
        userRepository.fetchVerifiedAreaList()
            .onSuccess {
                reduce {
                    LocalVerificationUiState.Success(verificationAreaList = it)
                }
            }
            .onFailure {
                LocalVerificationUiState.LoadFailed
            }
    }

    fun retry() = intent {
        reduce { LocalVerificationUiState.Loading }
        fetchVerifiedAreaList()
    }

    private fun showAreaDeleteFailDialog() = intent {
        runOn<LocalVerificationUiState.Success> {
            reduce {
                state.copy(showAreaDeleteFailDialog = true)
            }
        }
    }

    fun dismissAreaDeleteFailDialog() = intent {
        runOn<LocalVerificationUiState.Success> {
            reduce {
                state.copy(showAreaDeleteFailDialog = false)
            }
        }
    }

    fun showEditAreaDialog() = intent {
        runOn<LocalVerificationUiState.Success> {
            reduce {
                state.copy(showEditAreaDialog = true)
            }
        }
    }

    fun dismissEditAreaDialog() = intent {
        runOn<LocalVerificationUiState.Success> {
            reduce {
                state.copy(showEditAreaDialog = false)
            }
        }
    }

    fun deleteVerifiedArea(verifiedAreaId: Long) = intent {
        userRepository.deleteVerifiedArea(verifiedAreaId)
            .onSuccess {
                fetchVerifiedAreaList()
            }
            .onFailure { error ->
                when (error) {
                    is DeleteVerifiedAreaError.InvalidVerifiedArea -> {
                        Timber.e(TAG, "유효하지 않은 인증 지역입니다.")
                        postSideEffect(LocalVerificationSideEffect.ShowUnKnownErrorToast)
                    }

                    is DeleteVerifiedAreaError.VerifiedAreaLimitViolation -> {
                        Timber.e(TAG, "인증 지역은 최소 1개 이상 존재해야 합니다.")
                        showEditAreaDialog()
                    }

                    is DeleteVerifiedAreaError.PeriodRestrictedDeleteError -> {
                        Timber.e(TAG, "인증일로부터 1주 이상 3개월 미만인 지역은 삭제할 수 없습니다.")
                        showAreaDeleteFailDialog()
                    }

                    is DeleteVerifiedAreaError.VerifiedAreaNotFound -> {
                        Timber.e(TAG, "존재하지 않는 인증 동네입니다.")
                        postSideEffect(LocalVerificationSideEffect.ShowUnKnownErrorToast)
                    }

                    else -> {
                        Timber.e(TAG, error.message)
                        postSideEffect(LocalVerificationSideEffect.ShowUnKnownErrorToast)
                    }
                }
            }
    }

    fun onNavigateToSettingsScreen() = intent {
        postSideEffect(LocalVerificationSideEffect.NavigateToSettingsScreen)
    }

    fun onNavigateToAreaVerification(verifiedAreaId: Long) = intent {
        postSideEffect(LocalVerificationSideEffect.NavigateToAreaVerification(verifiedAreaId))
    }

    companion object {
        const val TAG = "LocalVerificationViewModel"
    }
}

sealed interface LocalVerificationUiState {
    data class Success(
        val selectedAreaId: Long? = null,
        val verificationAreaList: List<Area>,
        val showAreaDeleteFailDialog: Boolean = false,
        val showEditAreaDialog: Boolean = false
    ) : LocalVerificationUiState

    data object Loading : LocalVerificationUiState
    data object LoadFailed : LocalVerificationUiState
}

sealed interface LocalVerificationSideEffect {
    data object ShowUnKnownErrorToast : LocalVerificationSideEffect
    data object NavigateToSettingsScreen : LocalVerificationSideEffect
    data class NavigateToAreaVerification(val verifiedAreaId: Long) : LocalVerificationSideEffect
}