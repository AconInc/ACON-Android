package com.acon.acon.data.repository

import com.acon.acon.data.datasource.remote.ProfileRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.profile.ValidateNicknameError
import com.acon.acon.domain.model.profile.PreSignedUrl
import com.acon.acon.domain.model.profile.Profile
import com.acon.acon.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override suspend fun fetchProfile(): Result<Profile> {
        return runCatchingWith() {
            profileRemoteDataSource.fetchProfile().toProfile()
        }
    }

    override suspend fun getPreSignedUrl(): Result<PreSignedUrl> {
        return runCatchingWith() {
            profileRemoteDataSource.getPreSignedUrl().toPreSignedUrl()
        }
    }

    override suspend fun validateNickname(nickname: String): Result<Unit> {
        return runCatchingWith(*ValidateNicknameError.createErrorInstances()) {
            profileRemoteDataSource.validateNickname(nickname)
        }
    }

    override suspend fun updateProfile(fileName: String, nickname: String, birthday: String?): Result<Unit> {
        return runCatchingWith() {
            profileRemoteDataSource.updateProfile(fileName, nickname, birthday)
        }
    }
}