package com.acon.acon.domain.repository

interface TokenRepository {
    suspend fun saveAccessToken(accessToken: String): Result<Unit>
    suspend fun saveRefreshToken(refreshToken: String): Result<Unit>
    suspend fun saveAreaVerification(state: Boolean): Result<Unit>
    suspend fun getAccessToken(): Result<String?>
    suspend fun getRefreshToken(): Result<String?>
    suspend fun getAreaVerification(): Result<Boolean>
    suspend fun removeAccessToken(): Result<Unit>
    suspend fun removeRefreshToken(): Result<Unit>
    suspend fun removeAreaVerification(): Result<Unit>
    suspend fun removeAllToken(): Result<Unit>
}