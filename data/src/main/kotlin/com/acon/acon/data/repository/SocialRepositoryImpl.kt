package com.acon.acon.data.repository

import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.datasource.remote.TokenRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.type.SocialType
import javax.inject.Inject

class SocialRepositoryImpl @Inject constructor(
    private val tokenRemoteDataSource: TokenRemoteDataSource,
    private val userRepository: UserRepository,
    private val tokenLocalDataSource: TokenLocalDataSource
) : SocialRepository {
    override suspend fun signIn(): Result<Unit> {
        return runCatchingWith() {
            val idToken = tokenRemoteDataSource.signIn().getOrThrow()
            tokenLocalDataSource.saveGoogleIdToken(idToken)

            userRepository.postLogin(
                socialType = SocialType.GOOGLE, idToken = idToken
            ).getOrThrow()
        }
    }
}
