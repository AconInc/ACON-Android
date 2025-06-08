package com.acon.acon.domain.repository

import com.acon.acon.domain.model.user.VerificationStatus

interface SocialRepository {
    suspend fun googleSignIn(): Result<VerificationStatus>
}