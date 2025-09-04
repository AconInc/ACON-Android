package com.acon.acon.domain.usecase

import com.acon.acon.domain.error.profile.ValidateNicknameError
import com.acon.acon.domain.repository.ProfileRepository
import javax.inject.Inject

class ValidateNicknameUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    private val nicknameValidationRegex by lazy {
        Regex("[^a-z0-9_.]")
    }

    suspend operator fun invoke(nickname: String) : Result<Unit> {
        return when {
            nickname.isEmpty() -> Result.failure(ValidateNicknameError.EmptyInput())
            nickname.length > 14 -> Result.failure(ValidateNicknameError.InputLengthExceeded())
            nickname.containsInvalidCharacters() -> Result.failure(ValidateNicknameError.InvalidFormat())
            else -> profileRepository.validateNickname(nickname)
        }
    }

    private fun String.containsInvalidCharacters(): Boolean {
        return nicknameValidationRegex.containsMatchIn(this)
    }
}