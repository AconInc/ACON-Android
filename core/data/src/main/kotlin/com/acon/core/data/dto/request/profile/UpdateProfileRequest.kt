package com.acon.core.data.dto.request.profile

import com.acon.acon.core.model.model.profile.BirthDateStatus
import com.acon.acon.core.model.model.profile.Profile
import com.acon.acon.core.model.model.profile.ProfileImageStatus

data class UpdateProfileRequest(
    val nickname: String,
    val birthDate: String?,
    val image: String?
)

fun Profile.toUpdateProfileRequest() : UpdateProfileRequest {
    val requestNickname = nickname
    val requestBirthDate: String? = when(birthDate) {
        is BirthDateStatus.Specified -> with((birthDate as BirthDateStatus.Specified).date) {
            "$year.$month.$dayOfMonth"
        }
        BirthDateStatus.NotSpecified -> null
    }
    val requestImage: String? = when(image) {
        is ProfileImageStatus.Custom -> (image as ProfileImageStatus.Custom).url
        else -> null
    }

    return UpdateProfileRequest(requestNickname, requestBirthDate, requestImage)
}