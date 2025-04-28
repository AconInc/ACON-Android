package com.acon.acon.data.repository

import com.acon.acon.data.datasource.local.AconAppLocalDataSource
import com.acon.acon.data.datasource.remote.AconAppRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.repository.AconAppRepository
import javax.inject.Inject

class AconAppRepositoryImpl @Inject constructor(
    private val aconAppRemoteDataSource: AconAppRemoteDataSource,
    private val aconAppLocalDataSource: AconAppLocalDataSource
) : AconAppRepository {
    override suspend fun fetchShouldUpdateApp(): Result<Boolean> {
        return runCatchingWith {
            aconAppRemoteDataSource.fetchShouldUpdateApp()
        }
    }

    override suspend fun setUpdatePostponeTime(time: Long): Result<Unit> {
        return runCatchingWith {
            aconAppLocalDataSource.setUpdatePostponeTime(time)
        }
    }

    override suspend fun getUpdatePostponeTime(): Result<Long> {
        return runCatchingWith {
            aconAppLocalDataSource.getUpdatePostponeTime()
        }
    }
}