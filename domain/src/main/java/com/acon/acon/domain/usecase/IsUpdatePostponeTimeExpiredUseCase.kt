package com.acon.acon.domain.usecase

import com.acon.acon.domain.repository.AconAppRepository
import javax.inject.Inject

/**
 * 업데이트 진행 여부 선택 후 24시간 지났는지 확인하는 UseCase
 */
class IsUpdatePostponeTimeExpiredUseCase @Inject constructor(
    private val aconAppRepository: AconAppRepository
) {

    suspend operator fun invoke(): Boolean {
        val lastTime = aconAppRepository.getUpdatePostponeTime().getOrElse { return false }
        return System.currentTimeMillis() - lastTime >= ONE_DAY_IN_MILLIS
    }

    companion object {
        private const val ONE_DAY_IN_MILLIS = 86_400_000L
    }
}
