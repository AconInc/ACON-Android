package com.acon.acon.data.repository

import com.acon.acon.data.SessionManager
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val sessionManager: SessionManager
) : TokenRepository {
    override suspend fun getAccessToken(): Result<String?> {
        return runCatchingWith() {
            tokenLocalDataSource.getAccessToken()
        }
    }

    override suspend fun getAreaVerification(): Result<Boolean> {
        return runCatchingWith() {
            tokenLocalDataSource.getAreaVerification()
        }
    }
}