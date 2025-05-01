package com.acon.acon.domain.repository

import com.acon.acon.domain.model.profile.PreSignedUrl
import com.acon.acon.domain.model.profile.Profile

interface ProfileRepository {
    suspend fun fetchProfile(): Result<Profile>

    suspend fun getPreSignedUrl(): Result<PreSignedUrl>

    suspend fun validateNickname(nickname: String): Result<Unit>

    suspend fun updateProfile(fileName: String, nickname: String, birthday: String?): Result<Unit>

}