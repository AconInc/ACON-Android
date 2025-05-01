package com.acon.acon.domain.model.user

import androidx.compose.runtime.Immutable

@Immutable
data class VerificationStatus(
    val externalUUID: String,
    val hasVerifiedArea: Boolean
)