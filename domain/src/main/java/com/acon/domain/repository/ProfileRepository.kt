package com.acon.domain.repository

import com.acon.domain.model.profile.Profile

interface ProfileRepository {
    suspend fun fetchProfile(): Result<Profile>
}