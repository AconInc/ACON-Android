package com.acon.acon.feature.upload

import androidx.lifecycle.viewModelScope
import com.acon.acon.domain.repository.UploadRepository
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.error.upload.GetVerifySpotLocationError
import com.acon.acon.domain.error.user.GetSuggestionsError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val uploadRepository: UploadRepository
) : BaseContainerHost<UploadState, UploadSideEffect>() {

    override val container = container<UploadState, UploadSideEffect>(UploadState())

    val searchFlow = MutableStateFlow("")

    init {
        getDotoriCount()
        observeSearch()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() = intent {
        searchFlow
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { keyword ->
                if (keyword.isEmpty()) {
                    reduce {
                        state.copy(
                            searchResults = persistentListOf(),
                            isLoading = false
                        )
                    }
                } else {
                    reduce {
                        state.copy(
                            searchResults = persistentListOf(),
                            isLoading = true
                        )
                    }
                    uploadRepository.getKeyWord(keyword)
                        .onSuccess { keyWord ->
                            reduce {
                                state.copy(
                                    searchResults = keyWord.spotList.toPersistentList(),
                                    isLoading = false
                                )
                            }
                        }
                        .onFailure {
                            reduce {
                                state.copy(
                                    searchResults = persistentListOf(),
                                    isLoading = false
                                )
                            }
                        }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: UploadIntent) {
        when (intent) {
            is UploadIntent.SelectLocation -> {
                intent {
                    reduce { state.copy(selectedLocation = intent.location) }
                }
            }

            is UploadIntent.VerifyLocation -> {
                verifyLocation(
                    spotId = intent.spotId,
                    latitude = intent.latitude,
                    longitude = intent.longitude
                )
            }

            is UploadIntent.ResetVerification -> intent {
                reduce {
                    state.copy(locationVerificationResult = null)
                }
            }

            is UploadIntent.OnNextStep -> {
                intent {
                    getDotoriCount()
                    reduce { state.copy(currentStep = UploadStep.UPLOAD_REVIEW) }
                }
            }

            is UploadIntent.SelectDotori -> selectDotori(intent.index)
            is UploadIntent.DeselectDotori -> deselectDotori(intent.index)
            is UploadIntent.NavigateBack -> navigateBack()
            is UploadIntent.UploadDotori -> uploadDotori()
            is UploadIntent.LoadSuggestions -> loadSuggestions(intent.latitude, intent.longitude)
        }
    }

    private fun selectDotori(index: Int) = intent {
        if (index >= state.selectedCount && index < state.maxCount) {
            if (state.ownedDotoriCount < index + 1) {
                reduce { state.copy(showInsufficientDotoriSnackbar = true) }
                delay(2000)
                reduce { state.copy(showInsufficientDotoriSnackbar = false) }
                return@intent
            }

            reduce { state.copy(animatingIndex = null) }
            delay(50)
            reduce {
                state.copy(
                    selectedCount = index + 1,
                    isButtonEnabled = true,
                    animatingIndex = index
                )
            }

            delay(3000)
            reduce {
                if (state.animatingIndex == index) {
                    state.copy(animatingIndex = null)
                } else {
                    state
                }
            }
        }
    }

    private fun deselectDotori(index: Int) = intent {
        if (state.selectedCount > 0) {
            reduce {
                state.copy(animatingIndex = null)
            }

            delay(50)

            reduce {
                state.copy(
                    selectedCount = index + 1,
                    isButtonEnabled = true,
                    animatingIndex = index
                )
            }

            delay(3000)
            reduce {
                if (state.animatingIndex == index) {
                    state.copy(animatingIndex = null)
                } else {
                    state
                }
            }
        }
    }

    private fun getDotoriCount() = intent {
        uploadRepository.getDotoriCount()
            .onSuccess { dotoriCount ->
                reduce {
                    state.copy(ownedDotoriCount = dotoriCount.count)
                }
            }
            .onFailure { }
    }

    private fun loadSuggestions(latitude: Double, longitude: Double) = intent {
        uploadRepository.getSuggestions(latitude, longitude)
            .onSuccess { suggestions ->
                reduce {
                    state.copy(suggestions = suggestions.suggestionList.toPersistentList())
                }
            }
            .onFailure { error ->
                if (error is GetSuggestionsError.OutOfServiceAreaError) {
                    delay(300)
                    reduce {
                        state.copy(
                            showOutOfServiceDialog = true,
                            suggestions = persistentListOf()
                        )
                    }
                } else {
                    reduce {
                        state.copy(suggestions = persistentListOf())
                    }
                }
            }

    }

    private fun uploadDotori() = intent {
        state.selectedLocation?.let { location ->
            uploadRepository.postReview(
                spotId = location.spotId,
                acornCount = state.selectedCount
            ).onSuccess {
                postSideEffect(UploadSideEffect.NavigateToSuccess)
            }.onFailure { }
        }
    }

    private fun verifyLocation(spotId: Long, latitude: Double, longitude: Double) = intent {
        uploadRepository.getVerifySpotLocation(spotId, latitude, longitude)
            .onSuccess { verification ->
                reduce {
                    state.copy(
                        isLocationVerified = verification.success,
                        locationVerificationResult = verification.success
                    )
                }
            }
            .onFailure { error ->
                if(error is GetVerifySpotLocationError.OutOfServiceAreaError) {
                    delay(300)
                    reduce {
                        state.copy(
                            showOutOfServiceDialog = true,
                        )
                    }
                } else {
                    reduce {
                        state.copy(
                            isLocationVerified = false,
                            locationVerificationResult = false,
                            selectedLocation = null
                        )
                    }
                }
            }
    }

    fun onOutOfServiceDialogStateChange(show: Boolean) = intent {
        reduce {
            state.copy(showOutOfServiceDialog = show)
        }
    }

    fun onLocationSearchBottomSheetStateChange(show: Boolean) = intent {
        reduce {
            state.copy(showLocationSearchBottomSheet = show)
        }
    }

    private fun navigateBack() = intent {
        postSideEffect(UploadSideEffect.NavigateBack)
    }
}
