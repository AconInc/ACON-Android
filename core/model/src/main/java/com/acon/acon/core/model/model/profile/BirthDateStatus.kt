package com.acon.acon.core.model.model.profile

import java.time.LocalDate

sealed interface BirthDateStatus {

    data object NotSpecified : BirthDateStatus
    data class Specified(val date: LocalDate) : BirthDateStatus
}