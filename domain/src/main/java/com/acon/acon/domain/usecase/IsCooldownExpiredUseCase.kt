package com.acon.acon.domain.usecase

import com.acon.acon.core.model.type.UserActionType
import com.acon.acon.domain.repository.TimeRepository
import javax.inject.Inject

class IsCooldownExpiredUseCase @Inject constructor(
    private val timeRepository: TimeRepository
) {

    suspend operator fun invoke(action: UserActionType, cooldownSeconds: Long, currentTimestamp: Long = System.currentTimeMillis()) : Boolean {
        timeRepository.getUserActionTime(action).onSuccess {
            if (it == null)
                return true

            val elapsedTime = currentTimestamp - it
            return elapsedTime >= cooldownSeconds * 1000
        }.onFailure {
            return false
        }

        return false
    }
}