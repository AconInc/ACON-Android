package com.acon.acon.domain.repository


interface TokenRepository {
    suspend fun getAccessToken(): Result<String?>
}