package com.acon.acon.domain.repository

interface SocialRepository {
    suspend fun signIn(): Result<Unit>
}