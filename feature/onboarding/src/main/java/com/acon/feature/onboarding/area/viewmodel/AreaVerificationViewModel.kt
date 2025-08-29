package com.acon.feature.onboarding.area.viewmodel

import com.acon.acon.core.model.type.UserActionType
import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.domain.repository.TimeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel(assistedFactory = AreaVerificationViewModel.Factory::class)
class AreaVerificationViewModel @AssistedInject constructor(
    private val timeRepository: TimeRepository,
    @Assisted private val shouldShowSkipButton: Boolean
) : BaseContainerHost<AreaVerificationState, AreaVerificationSideEffect>() {

    override val container = container<AreaVerificationState, AreaVerificationSideEffect>(
        AreaVerificationState(
            shouldShowSkipButton = shouldShowSkipButton
        )
    )

    fun onNextButtonClick() = intent {
        postSideEffect(
            AreaVerificationSideEffect.NavigateToVerifyInMap
        )
    }

    fun onSkipClicked() = intent {
        postSideEffect(AreaVerificationSideEffect.NavigateToChooseDislikes)
        timeRepository.saveUserActionTime(UserActionType.SKIP_AREA_VERIFICATION, System.currentTimeMillis())
    }

    @AssistedFactory
    interface Factory {
        fun create(shouldShowSkipButton: Boolean): AreaVerificationViewModel
    }
}

data class AreaVerificationState(
    val shouldShowSkipButton: Boolean
)

sealed interface AreaVerificationSideEffect {

    data class NavigateToAppLocationSettings(
        val packageName: String
    ) : AreaVerificationSideEffect

    data class NavigateToSystemLocationSettings(
        val packageName: String
    ) : AreaVerificationSideEffect

    data object NavigateToVerifyInMap: AreaVerificationSideEffect

    data class ShowErrorToast(val errorMessage: String) : AreaVerificationSideEffect

    data object NavigateToChooseDislikes : AreaVerificationSideEffect
}