package com.acon.acon.domain.repository


interface TokenRepository {
    suspend fun getAccessToken(): Result<String?>
    suspend fun getAreaVerification(): Result<Boolean>
}