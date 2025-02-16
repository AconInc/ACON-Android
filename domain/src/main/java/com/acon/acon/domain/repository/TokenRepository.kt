package com.acon.acon.domain.repository

interface TokenRepository {
    suspend fun saveGoogleIdToken(accessToken: String): Result<Unit>
    suspend fun saveAccessToken(accessToken: String): Result<Unit>
    suspend fun saveRefreshToken(refreshToken: String): Result<Unit>
    suspend fun getGoogleIdToken(): Result<String?>
    suspend fun getAccessToken(): Result<String?>
    suspend fun getRefreshToken(): Result<String?>
    suspend fun removeGoogleIdToken(): Result<Unit>
    suspend fun removeAccessToken(): Result<Unit>
    suspend fun removeRefreshToken(): Result<Unit>
    suspend fun removeAllToken(): Result<Unit>
}