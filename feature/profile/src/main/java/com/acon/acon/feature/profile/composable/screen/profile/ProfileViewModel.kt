package com.acon.acon.feature.profile.composable.screen.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.feature.common.base.BaseContainerHost
import com.acon.acon.domain.model.profile.VerifiedArea
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.UserType
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

    val updateProfileState = profileRepository.getProfileType()

    override val container =
        container<ProfileUiState, ProfileUiSideEffect>(ProfileUiState.Loading) {
            userRepository.getUserType().collect {
                when(it) {
                    UserType.GUEST -> reduce { ProfileUiState.Guest() }
                    else -> fetchUserProfileInfo()
                }
            }
        }

    fun resetUpdateProfileType() {
        viewModelScope.launch {
            profileRepository.resetProfileType()
        }
    }

    fun googleLogin(socialRepository: SocialRepository) = intent {
        onBottomSheetShowStateChange(false)
        socialRepository.googleLogin()
            .onSuccess {
                if(it.hasVerifiedArea) {
                    userRepository.updateLocalVerificationType(true)
                    postSideEffect(ProfileUiSideEffect.OnNavigateToSpotListScreen)
                } else {
                    userRepository.updateLocalVerificationType(false)
                    postSideEffect(ProfileUiSideEffect.OnNavigateToAreaVerificationScreen)
                }
            }.onFailure { error ->
                when (error) {
                    is CancellationException -> {
                        reduce { ProfileUiState.Guest() }
                    }
                    is NoSuchElementException -> {
                        reduce { ProfileUiState.Guest() }
                    }
                    is SecurityException -> {
                        reduce { ProfileUiState.Guest() }
                    }
                    else -> {
                        reduce { ProfileUiState.Guest() }
                    }
                }
            }
    }

     fun fetchUserProfileInfo() = intent {
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

    fun onBookmark() = intent {
        postSideEffect(ProfileUiSideEffect.OnNavigateToBookmarkScreen)
    }

    fun onSettings() = intent {
        postSideEffect(ProfileUiSideEffect.OnNavigateToSettingsScreen)
    }

    fun onEditProfile() = intent {
        postSideEffect(ProfileUiSideEffect.OnNavigateToProfileEditScreen)
    }

    fun onBottomSheetShowStateChange(show: Boolean) = intent {
        runOn<ProfileUiState.Guest> {
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

    data class Guest(
        val showLoginBottomSheet: Boolean = false
    ) : ProfileUiState
}

sealed interface ProfileUiSideEffect {
    data object OnNavigateToBookmarkScreen : ProfileUiSideEffect
    data object OnNavigateToSpotListScreen : ProfileUiSideEffect
    data object OnNavigateToSettingsScreen : ProfileUiSideEffect
    data object OnNavigateToProfileEditScreen : ProfileUiSideEffect
    data object OnNavigateToAreaVerificationScreen : ProfileUiSideEffect
}
