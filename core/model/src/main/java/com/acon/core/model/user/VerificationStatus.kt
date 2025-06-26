package com.acon.core.model.user

import androidx.compose.runtime.Immutable

@Immutable
data class VerificationStatus(
    val externalUUID: String,
    val hasVerifiedArea: Boolean
)