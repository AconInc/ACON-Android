package com.acon.core.data.dto.request.profile

data class UpdateProfileRequest(
    val nickname: String,
    val birthDate: String?,
    val image: String?
)
