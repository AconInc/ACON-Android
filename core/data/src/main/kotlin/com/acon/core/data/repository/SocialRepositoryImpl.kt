package com.acon.core.data.repository

import com.acon.core.data.datasource.remote.TokenRemoteDataSource
import com.acon.core.data.error.runCatchingWith
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
    override suspend fun googleSignIn(): Result<VerificationStatus> {
        return runCatchingWith(PostSignInError()) {
            val idToken = tokenRemoteDataSource.googleSignIn().getOrThrow()

            userRepository.signIn(
                socialType = SocialType.GOOGLE, idToken = idToken
            ).getOrThrow()
        }
    }
}
