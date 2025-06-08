package com.acon.acon.data.repository

import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
) : TokenRepository {
    override suspend fun getAccessToken(): Result<String?> {
        return runCatchingWith() {
            tokenLocalDataSource.getAccessToken()
        }
    }
}