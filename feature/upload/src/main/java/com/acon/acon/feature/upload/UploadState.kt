package com.acon.acon.feature.upload

import com.acon.acon.domain.model.upload.SpotListItem
import com.acon.acon.domain.model.upload.Suggestion
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class UploadState(
    val selectedLocation: SpotListItem? = null,
    val currentStep: UploadStep = UploadStep.UPLOAD_SEARCH,
    val selectedCount: Int = 0,
    val maxCount: Int = 5,
    val isButtonEnabled: Boolean = false,
    val animatingIndex: Int? = null,
    val isLocationSearchVisible: Boolean = false,
    val ownedDotoriCount: Int = 0,
    val searchResults: PersistentList<SpotListItem> = persistentListOf(),
    val suggestions: PersistentList<Suggestion> = persistentListOf(),
    val isLocationVerified: Boolean = false,
    val locationVerificationResult: Boolean? = null,
    val showInsufficientDotoriSnackbar: Boolean = false,
    val isLoading: Boolean = false,
    val showOutOfServiceDialog: Boolean = false,
    val showLocationSearchBottomSheet: Boolean = false
)
