package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.response.app.ShouldUpdateResponse
import com.acon.acon.data.api.remote.noauth.AconAppNoAuthApi
import javax.inject.Inject

class AconAppRemoteDataSource @Inject constructor(
    private val aconAppNoAuthApi: AconAppNoAuthApi
) {
    suspend fun fetchShouldUpdateApp(currentVersion: String): ShouldUpdateResponse {
        return aconAppNoAuthApi.fetchShouldUpdateApp(currentVersion)
    }
}