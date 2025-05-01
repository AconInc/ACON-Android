package com.acon.acon.feature.profile.composable.screen.profile

import androidx.compose.runtime.Immutable
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.profile.VerifiedArea
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository
) : BaseContainerHost<ProfileUiState, ProfileUiSideEffect>() {

    override val container =
        container<ProfileUiState, ProfileUiSideEffect>(ProfileUiState.Loading) {
            userRepository.getUserType().collect {
                when(it) {
                    UserType.GUEST -> reduce { ProfileUiState.Guest() }
                    else -> fetchUserProfileInfo()
                }
            }
        }

    fun googleLogin(socialRepository: SocialRepository) = intent {
        socialRepository.googleLogin()
            .onSuccess {
                if(it.hasVerifiedArea) {
                    postSideEffect(ProfileUiSideEffect.OnNavigateToSpotListScreen)
                } else {
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
    data object OnNavigateToSpotListScreen : ProfileUiSideEffect
    data object OnNavigateToSettingsScreen : ProfileUiSideEffect
    data object OnNavigateToProfileEditScreen : ProfileUiSideEffect
    data object OnNavigateToAreaVerificationScreen : ProfileUiSideEffect
    data object OnTermOfUse : ProfileUiSideEffect
    data object OnPrivatePolicy : ProfileUiSideEffect
}
