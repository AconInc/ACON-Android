package com.acon.acon.feature.profile.composable.screen.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.acon.domain.model.profile.ProfileInfo
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.type.UserType
import com.acon.feature.common.base.BaseContainerHost
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
        container<ProfileUiState, ProfileUiSideEffect>(ProfileUiState.Loading) {
            userType.collect {
                when(it) {
                    UserType.GUEST -> reduce { ProfileUiState.Guest }
                    else -> fetchUserProfileInfo()
                }
            }
        }

    fun resetUpdateProfileType() {
        viewModelScope.launch {
            profileRepository.resetProfileType()
        }
    }

     fun fetchUserProfileInfo() = intent {
         profileRepository.fetchProfile()
             .reduceResult(
                 onSuccess = { ProfileUiState.Success(profileInfo = it) },
                 onFailure = { ProfileUiState.LoadFailed }
             )
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
}
