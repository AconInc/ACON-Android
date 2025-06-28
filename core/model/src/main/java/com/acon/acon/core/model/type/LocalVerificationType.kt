package com.acon.acon.core.model.type

sealed class VerificationState {
    data object Loading : com.acon.acon.core.model.type.VerificationState()
    data class Loaded(val type: com.acon.acon.core.model.type.LocalVerificationType) : com.acon.acon.core.model.type.VerificationState()
}

enum class LocalVerificationType {
    VERIFIED, NOT_VERIFIED
}
