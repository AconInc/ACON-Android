package com.acon.android.data.repository

import com.acon.android.data.datasource.local.TokenLocalDataSource
import com.acon.android.data.datasource.remote.TokenRemoteDataSource
import com.acon.android.data.error.runCatchingWith
import com.acon.android.domain.repository.AuthRepository
import com.acon.android.domain.repository.SocialRepository
import com.acon.android.domain.type.SocialType
import javax.inject.Inject

class SocialRepositoryImpl @Inject constructor(
    private val tokenRemoteDataSource: TokenRemoteDataSource,
    private val authRepository: AuthRepository,
    private val tokenLocalDataSource: TokenLocalDataSource
) : SocialRepository {
    override suspend fun signIn(): Result<Unit> {
        return runCatchingWith() {
            val idToken = tokenRemoteDataSource.signIn().getOrThrow()
            tokenLocalDataSource.saveGoogleIdToken(idToken)

            authRepository.postLogin(
                socialType = SocialType.GOOGLE, idToken = idToken
            ).getOrThrow()
        }
    }
}
