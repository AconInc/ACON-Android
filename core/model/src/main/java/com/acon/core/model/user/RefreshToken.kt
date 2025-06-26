package com.acon.core.model.user

data class RefreshToken(
    val accessToken: String?,
    val refreshToken: String?,
)
