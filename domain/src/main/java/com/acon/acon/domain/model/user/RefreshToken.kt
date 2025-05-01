package com.acon.acon.domain.model.user

data class RefreshToken(
    val accessToken: String?,
    val refreshToken: String?,
)
