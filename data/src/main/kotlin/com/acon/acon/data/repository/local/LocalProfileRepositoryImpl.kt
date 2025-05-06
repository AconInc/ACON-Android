package com.acon.acon.data.repository.local

import com.acon.acon.core.common.IODispatcher
import com.acon.acon.domain.repository.local.LocalProfileRepository
import com.acon.acon.domain.type.UpdateProfileType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class LocalProfileRepositoryImpl @Inject constructor(
    @IODispatcher private val scope: CoroutineScope
) : LocalProfileRepository {

    private val _updateProfileType = MutableStateFlow(UpdateProfileType.IDLE)
    private val updateProfileType = flow {
        emitAll(_updateProfileType)
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = UpdateProfileType.IDLE
    )

    override fun updateProfileType(type: UpdateProfileType) {
        _updateProfileType.value = type
    }

    override fun getProfileType(): Flow<UpdateProfileType> {
        return updateProfileType
    }

    override suspend fun resetProfileType() {
        _updateProfileType.emit(UpdateProfileType.IDLE)
    }
}