package com.acon.acon.data.repository

import com.acon.acon.data.SessionManager
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val sessionManager: SessionManager
): TokenRepository {

    override suspend fun saveAccessToken(accessToken: String): Result<Unit> {
        return runCatchingWith() {
            tokenLocalDataSource.saveAccessToken(accessToken)
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String): Result<Unit> {
        return runCatchingWith() {
            tokenLocalDataSource.saveAccessToken(refreshToken)
        }
    }

    override suspend fun saveAreaVerification(state: Boolean): Result<Unit> {
        return runCatchingWith() {
            tokenLocalDataSource.saveAreaVerification(state)
        }
    }

    override suspend fun getAccessToken(): Result<String?> {
        return runCatchingWith() {
            tokenLocalDataSource.getAccessToken()
        }
    }

    override suspend fun getRefreshToken(): Result<String?> {
        return runCatchingWith() {
            tokenLocalDataSource.getRefreshToken()
        }
    }

    override suspend fun getAreaVerification(): Result<Boolean> {
        return runCatchingWith() {
            tokenLocalDataSource.getAreaVerification()
        }
    }

    override suspend fun removeAccessToken(): Result<Unit> {
        return runCatchingWith() {
            tokenLocalDataSource.removeAccessToken()
        }
    }

    override suspend fun removeRefreshToken(): Result<Unit> {
        return runCatchingWith() {
            tokenLocalDataSource.removeRefreshToken()
        }
    }

    override suspend fun removeAreaVerification(): Result<Unit> {
        return runCatchingWith() {
            tokenLocalDataSource.removeAreaVerification()
        }
    }

    override suspend fun removeAllToken(): Result<Unit> {
        return runCatchingWith() {
            sessionManager.clearSession()
        }
    }
}