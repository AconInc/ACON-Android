package com.acon.acon.domain.repository

interface AconAppRepository {
    suspend fun fetchShouldUpdateApp(): Result<Boolean>
}