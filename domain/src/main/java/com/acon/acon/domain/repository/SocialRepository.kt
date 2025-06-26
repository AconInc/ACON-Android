package com.acon.acon.domain.repository

import com.acon.core.model.user.VerificationStatus

interface SocialRepository {
    suspend fun googleSignIn(): Result<VerificationStatus>
}