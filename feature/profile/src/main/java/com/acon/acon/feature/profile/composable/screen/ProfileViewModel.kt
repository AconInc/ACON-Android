package com.acon.acon.feature.profile.composable.screen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.model.profile.VerifiedArea
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.repository.SocialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository
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
                if(it.hasVerifiedArea) {
                    postSideEffect(ProfileUiSideEffect.OnNavigateToSpotListScreen)
                } else {
                    postSideEffect(ProfileUiSideEffect.OnNavigateToAreaVerificationScreen)
                }
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

    private fun fetchUserProfileInfo() = intent {
        viewModelScope.launch {
            profileRepository.fetchProfile()
                .onSuccess { profile ->
                    reduce {
                        ProfileUiState.Success(
                            profileImage = profile.image,
                            nickname = profile.nickname,
                            aconCount = profile.leftAcornCount,
                            verifiedArea = profile.verifiedAreaList
                        )
                    }
                }
                .onFailure {
                    reduce { ProfileUiState.LoadFailed }
                }
        }
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
        val profileImage: String,
        val nickname: String,
        val aconCount: Int,
        val verifiedArea: List<VerifiedArea>
    ) : ProfileUiState

    data object Loading : ProfileUiState
    data object LoadFailed : ProfileUiState

    data class GUEST(
        val showLoginBottomSheet: Boolean = false
    ) : ProfileUiState
}

sealed interface ProfileUiSideEffect {
    data object OnNavigateToSpotListScreen : ProfileUiSideEffect
    data object OnNavigateToSettingsScreen : ProfileUiSideEffect
    data object OnNavigateToProfileEditScreen : ProfileUiSideEffect
    data object OnNavigateToAreaVerificationScreen : ProfileUiSideEffect
    data object OnTermOfUse : ProfileUiSideEffect
    data object OnPrivatePolicy : ProfileUiSideEffect
}
