package com.acon.acon.data.repository

import com.acon.acon.data.datasource.remote.AconAppRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.app.FetchShouldUpdateError
import com.acon.acon.domain.repository.AconAppRepository
import javax.inject.Inject

class AconAppRepositoryImpl @Inject constructor(
    private val aconAppRemoteDataSource: AconAppRemoteDataSource
) : AconAppRepository {

    override suspend fun shouldUpdateApp(currentVersion: String): Result<Boolean> {
        return runCatchingWith(*FetchShouldUpdateError.createErrorInstances()) {
            aconAppRemoteDataSource.fetchShouldUpdateApp(currentVersion).shouldUpdate ?: false
        }
    }
}