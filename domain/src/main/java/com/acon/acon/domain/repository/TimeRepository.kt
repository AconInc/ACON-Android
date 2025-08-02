package com.acon.acon.domain.repository

import com.acon.acon.core.model.type.UserActionType

interface TimeRepository {

    suspend fun saveUserActionTime(action: UserActionType, timeStamp: Long): Result<Unit>
    suspend fun getUserActionTime(action: UserActionType): Result<Long?>
}