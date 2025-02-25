package com.acon.acon.feature.areaverification

import com.acon.acon.domain.model.area.Area

data class AreaVerificationState(
    val isNewLocationSelected: Boolean = false,
    val isButtonEnabled: Boolean = true,
    val isLocationPermissionGranted: Boolean = false,
    var showPermissionDialog: Boolean = false,
    val hasLocationPermission: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isLocationObtained: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val areaName: String = "",
    val verifiedArea: Area? = null,
    val verifiedAreaList: List<Area> = emptyList(),

    val isGPSEnabled: Boolean = false,
    val showGPSDialog: Boolean = false,
)
