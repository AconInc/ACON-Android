package com.acon.feature.upload

sealed interface UploadSideEffect {
    data object NavigateToSuccess : UploadSideEffect
    data object NavigateBack : UploadSideEffect
}
