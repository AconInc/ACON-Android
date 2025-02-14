package com.acon.android.feature.areaverification

import com.acon.android.domain.model.area.Area

data class AreaVerificationState(
    val isNewLocationSelected: Boolean = false,
    val isButtonEnabled: Boolean = true,
    val isLocationPermissionGranted: Boolean = false,
    val showPermissionDialog: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isLocationObtained: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val areaName: String = "",
    val verifiedArea: Area? = null
)
