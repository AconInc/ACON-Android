package com.acon.acon.domain.repository

interface AconAppRepository {
    suspend fun fetchShouldUpdateApp(): Result<Boolean>
    suspend fun setUpdatePostponeTime(time: Long): Result<Unit>
    suspend fun getUpdatePostponeTime(): Result<Long>
}