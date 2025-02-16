package com.acon.acon.domain.repository

import com.acon.acon.domain.type.SocialType
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    fun getLoginState(): StateFlow<Boolean>
    suspend fun postLogin(socialType: SocialType, idToken: String): Result<Unit>
}