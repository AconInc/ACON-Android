package com.acon.core.type

sealed class VerificationState {
    data object Loading : VerificationState()
    data class Loaded(val type: LocalVerificationType) : VerificationState()
}

enum class LocalVerificationType {
    VERIFIED, NOT_VERIFIED
}
