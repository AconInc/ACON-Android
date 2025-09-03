package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.profile.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfile() : Flow<Result<Profile>>
}