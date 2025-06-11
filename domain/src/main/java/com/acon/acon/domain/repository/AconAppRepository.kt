package com.acon.acon.domain.repository

interface AconAppRepository {
    suspend fun shouldUpdateApp(currentVersion: String): Result<Boolean>
}