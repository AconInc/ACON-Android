package com.acon.acon.core.model.model.profile

sealed interface ProfileImageStatus {

    data object Default : ProfileImageStatus
    data class Custom(val url: String) : ProfileImageStatus
}