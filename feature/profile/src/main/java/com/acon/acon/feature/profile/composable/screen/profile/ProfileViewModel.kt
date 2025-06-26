package com.acon.acon.feature.profile.composable.screen.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.core.model.profile.ProfileInfo
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.core.type.UserType
import com.acon.core.ui.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseContainerHost<ProfileUiState, ProfileUiSideEffect>() {

    val updateProfileState = profileRepository.getProfileType()

    override val container =
        container<ProfileUiState, ProfileUiSideEffect>(ProfileUiState.Success(ProfileInfo.Empty)) {
            userType.collect {
                when(it) {
                    UserType.GUEST -> reduce { ProfileUiState.Guest }
                    else -> {
                        profileRepository.fetchProfile().collect { profileInfoResult ->
                            profileInfoResult.onSuccess {
                                reduce { ProfileUiState.Success(profileInfo = it) }
                            }.onFailure {
                                postSideEffect(ProfileUiSideEffect.FailedToLoadProfileInfo)
                            }
                        }
                    }
                }
            }
        }

    fun resetUpdateProfileType() {
        viewModelScope.launch {
            profileRepository.resetProfileType()
        }
    }

    fun onSpotDetail(spotId: Long) = intent {
        postSideEffect(ProfileUiSideEffect.OnNavigateToSpotDetailScreen(spotId))
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
}

sealed interface ProfileUiState {
    @Immutable
    data class Success(
        val profileInfo: ProfileInfo
    ) : ProfileUiState

    data object Guest : ProfileUiState
}

sealed interface ProfileUiSideEffect {
    data class OnNavigateToSpotDetailScreen(val spotId: Long) : ProfileUiSideEffect
    data object OnNavigateToBookmarkScreen : ProfileUiSideEffect
    data object OnNavigateToSpotListScreen : ProfileUiSideEffect
    data object OnNavigateToSettingsScreen : ProfileUiSideEffect
    data object OnNavigateToProfileEditScreen : ProfileUiSideEffect

    data object FailedToLoadProfileInfo : ProfileUiSideEffect
}
