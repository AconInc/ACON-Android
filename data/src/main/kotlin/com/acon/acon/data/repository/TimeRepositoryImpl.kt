package com.acon.acon.data.repository

import com.acon.acon.core.model.type.UserActionType
import com.acon.acon.data.datasource.local.TimeLocalDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.repository.TimeRepository
import javax.inject.Inject

class TimeRepositoryImpl @Inject constructor(
    private val timeLocalDataSource: TimeLocalDataSource
) : TimeRepository {

    override suspend fun saveUserActionTime(action: UserActionType, timeStamp: Long): Result<Unit> {
        return runCatchingWith {
            timeLocalDataSource.saveUserActionTime(action, timeStamp)
        }
    }

    override suspend fun getUserActionTime(action: UserActionType): Result<Long?> {
        return runCatchingWith {
            timeLocalDataSource.getUserActionTime(action)
        }
    }
}