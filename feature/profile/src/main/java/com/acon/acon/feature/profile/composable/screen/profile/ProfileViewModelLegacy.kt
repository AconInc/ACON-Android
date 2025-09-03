package com.acon.acon.feature.profile.composable.screen.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.acon.domain.repository.ProfileRepositoryLegacy
import com.acon.acon.core.model.type.UserType
import com.acon.acon.core.ui.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepositoryLegacy: ProfileRepositoryLegacy
) : BaseContainerHost<ProfileUiStateLegacy, ProfileUiSideEffectLegacy>() {

    val updateProfileState = profileRepositoryLegacy.getProfileType()

    override val container =
        container<ProfileUiStateLegacy, ProfileUiSideEffectLegacy>(ProfileUiStateLegacy.Success(com.acon.acon.core.model.model.profile.ProfileInfoLegacy.Empty)) {
            userType.collect {
                when(it) {
                    UserType.GUEST -> reduce { ProfileUiStateLegacy.Guest }
                    else -> {
                        profileRepositoryLegacy.fetchProfile().collect { profileInfoResult ->
                            profileInfoResult.onSuccess {
                                reduce { ProfileUiStateLegacy.Success(profileInfoLegacy = it) }
                            }.onFailure {
                                postSideEffect(ProfileUiSideEffectLegacy.FailedToLoadProfileInfoLegacy)
                            }
                        }
                    }
                }
            }
        }

    fun resetUpdateProfileType() {
        viewModelScope.launch {
            profileRepositoryLegacy.resetProfileType()
        }
    }

    fun onSpotDetail(spotId: Long) = intent {
        postSideEffect(ProfileUiSideEffectLegacy.OnNavigateToSpotDetailScreen(spotId))
    }

    fun onBookmark() = intent {
        postSideEffect(ProfileUiSideEffectLegacy.OnNavigateToBookmarkScreen)
    }

    fun onSettings() = intent {
        postSideEffect(ProfileUiSideEffectLegacy.OnNavigateToSettingsScreen)
    }

    fun onEditProfile() = intent {
        postSideEffect(ProfileUiSideEffectLegacy.OnNavigateToProfileEditScreenLegacy)
    }
}

sealed interface ProfileUiStateLegacy {
    @Immutable
    data class Success(
        val profileInfoLegacy: com.acon.acon.core.model.model.profile.ProfileInfoLegacy
    ) : ProfileUiStateLegacy

    data object Guest : ProfileUiStateLegacy
}

sealed interface ProfileUiSideEffectLegacy {
    data class OnNavigateToSpotDetailScreen(val spotId: Long) : ProfileUiSideEffectLegacy
    data object OnNavigateToBookmarkScreen : ProfileUiSideEffectLegacy
    data object OnNavigateToSpotListScreen : ProfileUiSideEffectLegacy
    data object OnNavigateToSettingsScreen : ProfileUiSideEffectLegacy
    data object OnNavigateToProfileEditScreenLegacy : ProfileUiSideEffectLegacy

    data object FailedToLoadProfileInfoLegacy : ProfileUiSideEffectLegacy
}
