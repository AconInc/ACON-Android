package com.acon.acon.data.repository

import com.acon.acon.data.datasource.remote.TokenRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.user.PostSignInError
import com.acon.acon.core.model.model.user.VerificationStatus
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.core.model.type.SocialType
import javax.inject.Inject

class SocialRepositoryImpl @Inject constructor(
    private val tokenRemoteDataSource: TokenRemoteDataSource,
    private val userRepository: UserRepository,
) : SocialRepository {
    override suspend fun googleSignIn(): Result<com.acon.acon.core.model.model.user.VerificationStatus> {
        return runCatchingWith(*PostSignInError.createErrorInstances()) {
            val idToken = tokenRemoteDataSource.googleSignIn().getOrThrow()

            userRepository.signIn(
                socialType = com.acon.acon.core.model.type.SocialType.GOOGLE, idToken = idToken
            ).getOrThrow()
        }
    }
}
