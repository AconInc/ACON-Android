package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.response.app.ShouldUpdateResponse
import com.acon.acon.data.remote.AconAppApi
import javax.inject.Inject

class AconAppRemoteDataSource @Inject constructor(
    private val aconAppApi: AconAppApi
) {
    suspend fun fetchShouldUpdateApp(currentVersion: String): ShouldUpdateResponse {
        return aconAppApi.fetchShouldUpdateApp(currentVersion)
    }
}