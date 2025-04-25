package com.acon.acon.data.datasource.remote

import com.acon.acon.data.api.remote.AconAppApi
import javax.inject.Inject

class AconAppRemoteDataSource @Inject constructor(
    private val aconAppApi: AconAppApi
) {
    suspend fun fetchShouldUpdateApp(): Boolean {
        return aconAppApi.fetchShouldUpdateApp()
    }
}