package com.acon.acon.feature.profile.composable.screen

import androidx.compose.runtime.Immutable
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.repository.SocialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseContainerHost<ProfileUiState, ProfileUiSideEffect>() {

    override val container =
        container<ProfileUiState, ProfileUiSideEffect>(ProfileUiState.Loading) {
            userRepository.getLoginState().collect { isLoggedIn ->
                if (isLoggedIn) {
                    fetchUserProfileInfo()
                } else {
                    reduce { ProfileUiState.GUEST() }
                }
            }
        }

    fun googleLogin(socialRepository: SocialRepository) = intent {
        socialRepository.signIn()
            .onSuccess {
                postSideEffect(ProfileUiSideEffect.OnNavigateToAreaVerificationScreen)
            }.onFailure { error ->
                when (error) {
                    is CancellationException -> {
                        reduce { ProfileUiState.GUEST() }
                    }
                    is NoSuchElementException -> {
                        reduce { ProfileUiState.GUEST() }
                    }
                    is SecurityException -> {
                        reduce { ProfileUiState.GUEST() }
                    }
                    else -> {
                        reduce { ProfileUiState.GUEST() }
                    }
                }
            }
    }

    fun fetchUserProfileInfo() = intent {
        // TODO - 프로필 정보 조회 추가해야 함
        reduce { ProfileUiState.Success() }
    }

    fun onSettings() = intent {
        postSideEffect(ProfileUiSideEffect.OnNavigateToSettingsScreen)
    }

    fun onEditProfile() = intent {
        postSideEffect(ProfileUiSideEffect.OnNavigateToProfileEditScreen)
    }

    fun onTermOfUse() = intent {
        postSideEffect(ProfileUiSideEffect.OnTermOfUse)
    }

    fun onPrivatePolicy() = intent {
        postSideEffect(ProfileUiSideEffect.OnPrivatePolicy)
    }

    fun onBottomSheetShowStateChange(show: Boolean) = intent {
        runOn<ProfileUiState.GUEST> {
            reduce {
                state.copy(showLoginBottomSheet = show)
            }
        }
    }
}

sealed interface ProfileUiState {
    @Immutable
    data class Success(
        val profileImage: String ="",
        val aconCount: Int = 0,
        val verifiedArea: String = ""
    ) : ProfileUiState

    data object Loading : ProfileUiState
    data object LoadFailed : ProfileUiState

    data class GUEST(
        val showLoginBottomSheet: Boolean = false
    ) : ProfileUiState
}

sealed interface ProfileUiSideEffect {
    data object OnNavigateToSettingsScreen : ProfileUiSideEffect
    data object OnNavigateToProfileEditScreen : ProfileUiSideEffect
    data object OnNavigateToAreaVerificationScreen : ProfileUiSideEffect
    data object OnTermOfUse : ProfileUiSideEffect
    data object OnPrivatePolicy : ProfileUiSideEffect
}
