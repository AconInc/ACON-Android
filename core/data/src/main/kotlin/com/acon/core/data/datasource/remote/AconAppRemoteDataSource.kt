package com.acon.core.data.datasource.remote

import com.acon.core.data.dto.response.app.ShouldUpdateResponse
import com.acon.core.data.api.remote.noauth.AconAppNoAuthApi
import javax.inject.Inject

class AconAppRemoteDataSource @Inject constructor(
    private val aconAppNoAuthApi: AconAppNoAuthApi
) {
    suspend fun fetchShouldUpdateApp(currentVersion: String): ShouldUpdateResponse {
        return aconAppNoAuthApi.fetchShouldUpdateApp(currentVersion)
    }
}