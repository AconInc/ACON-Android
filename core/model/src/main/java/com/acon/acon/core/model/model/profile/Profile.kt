package com.acon.acon.core.model.model.profile

data class Profile(
    val nickname: String,
    val birthDate: BirthDateStatus,
    val image: ProfileImageStatus
)
