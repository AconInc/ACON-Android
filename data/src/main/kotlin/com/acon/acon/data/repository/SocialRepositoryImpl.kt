package com.acon.acon.data.repository

import com.acon.acon.data.datasource.remote.TokenRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.user.PostLoginError
import com.acon.acon.domain.model.user.VerificationStatus
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.SocialType
import javax.inject.Inject

class SocialRepositoryImpl @Inject constructor(
    private val tokenRemoteDataSource: TokenRemoteDataSource,
    private val userRepository: UserRepository,
) : SocialRepository {
    override suspend fun googleLogin(): Result<VerificationStatus> {
        return runCatchingWith(*PostLoginError.createErrorInstances()) {
            val idToken = tokenRemoteDataSource.googleLogin().getOrThrow()

            userRepository.login(
                socialType = SocialType.GOOGLE, idToken = idToken
            ).getOrThrow()
        }
    }
}
