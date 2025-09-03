package com.acon.acon.feature.verification.screen

import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.domain.error.area.DeleteVerifiedAreaError
import com.acon.acon.domain.repository.ProfileRepositoryLegacy
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class UserVerifiedAreasViewModel @Inject constructor(
    private val profileRepositoryLegacy: ProfileRepositoryLegacy
) : BaseContainerHost<UserVerifiedAreasUiState, UserVerifiedAreasSideEffect>() {

    override val container: Container<UserVerifiedAreasUiState, UserVerifiedAreasSideEffect> =
        container(UserVerifiedAreasUiState.Loading) {
            fetchVerifiedAreaList()
        }

    private fun fetchVerifiedAreaList() = intent {
        profileRepositoryLegacy.fetchVerifiedAreaList()
            .onSuccess {
                reduce {
                    UserVerifiedAreasUiState.Success(verificationAreaList = it)
                }
            }
            .onFailure {
                UserVerifiedAreasUiState.LoadFailed
            }
    }

    fun retry() = intent {
        reduce { UserVerifiedAreasUiState.Loading }
        fetchVerifiedAreaList()
    }

    private fun showAreaDeleteFailDialog() = intent {
        runOn<UserVerifiedAreasUiState.Success> {
            reduce {
                state.copy(showAreaDeleteFailDialog = true)
            }
        }
    }

    fun dismissAreaDeleteFailDialog() = intent {
        runOn<UserVerifiedAreasUiState.Success> {
            reduce {
                state.copy(showAreaDeleteFailDialog = false)
            }
        }
    }

    fun showEditAreaDialog() = intent {
        runOn<UserVerifiedAreasUiState.Success> {
            reduce {
                state.copy(showEditAreaDialog = true)
            }
        }
    }

    fun dismissEditAreaDialog() = intent {
        runOn<UserVerifiedAreasUiState.Success> {
            reduce {
                state.copy(showEditAreaDialog = false)
            }
        }
    }

    fun deleteVerifiedArea(verifiedAreaId: Long) = intent {
        profileRepositoryLegacy.deleteVerifiedArea(verifiedAreaId)
            .onSuccess {
                fetchVerifiedAreaList()
            }
            .onFailure { error ->
                when (error) {
                    is DeleteVerifiedAreaError.InvalidVerifiedArea -> {
                        Timber.e(TAG, "유효하지 않은 인증 지역입니다.")
                        postSideEffect(UserVerifiedAreasSideEffect.ShowUnKnownErrorToast)
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
                        postSideEffect(UserVerifiedAreasSideEffect.ShowUnKnownErrorToast)
                    }

                    else -> {
                        Timber.e(TAG, error.message)
                        postSideEffect(UserVerifiedAreasSideEffect.ShowUnKnownErrorToast)
                    }
                }
            }
    }

    fun onNavigateToSettingsScreen() = intent {
        postSideEffect(UserVerifiedAreasSideEffect.NavigateToSettingsScreen)
    }

    fun onNavigateToAreaVerification(verifiedAreaId: Long) = intent {
        postSideEffect(UserVerifiedAreasSideEffect.NavigateToAreaVerification(verifiedAreaId))
    }

    companion object {
        const val TAG = "LocalVerificationViewModel"
    }
}

sealed interface UserVerifiedAreasUiState {
    data class Success(
        val selectedAreaId: Long? = null,
        val verificationAreaList: List<com.acon.acon.core.model.model.area.Area>,
        val showAreaDeleteFailDialog: Boolean = false,
        val showEditAreaDialog: Boolean = false
    ) : UserVerifiedAreasUiState

    data object Loading : UserVerifiedAreasUiState
    data object LoadFailed : UserVerifiedAreasUiState
}

sealed interface UserVerifiedAreasSideEffect {
    data object ShowUnKnownErrorToast : UserVerifiedAreasSideEffect
    data object NavigateToSettingsScreen : UserVerifiedAreasSideEffect
    data class NavigateToAreaVerification(val verifiedAreaId: Long) : UserVerifiedAreasSideEffect
}