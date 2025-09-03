package com.acon.core.data.dto.response.profile

import com.acon.acon.core.model.model.profile.BirthDateStatus
import com.acon.acon.core.model.model.profile.Profile
import com.acon.acon.core.model.model.profile.ProfileImageStatus
import java.time.LocalDate

data class ProfileResponse(
    val nickname: String,
    val birthDate: String?,
    val image: String?
) {

    fun toProfile() : Profile {
        val nicknameOfModel = nickname
        val birthDateOfModel = birthDate?.let { dateString ->
            try {
                val (year, month, day) = dateString.split(".").map { it.toInt() }
                BirthDateStatus.Specified(LocalDate.of(year, month, day))
            } catch (_: Exception) {
                BirthDateStatus.NotSpecified
            }
        } ?: BirthDateStatus.NotSpecified
        val imageOfModel =
            if (image == null) ProfileImageStatus.Default else ProfileImageStatus.Custom(image)

        return Profile(nicknameOfModel, birthDateOfModel, imageOfModel)
    }
}
