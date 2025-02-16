package com.acon.data.repository

import com.acon.data.datasource.remote.ProfileRemoteDataSource
import com.acon.data.error.runCatchingWith
import com.acon.domain.model.profile.Profile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource
) {
    suspend fun fetchProfile(): Result<Profile> {
        return runCatchingWith() {
            profileRemoteDataSource.fetchProfile().toProfile()
        }
    }
}