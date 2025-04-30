package com.acon.acon.feature.profile.composable.screen.profileMod

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.acon.acon.domain.error.profile.ValidateNicknameError
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.feature.profile.composable.component.TextFieldStatus
import com.acon.acon.feature.profile.composable.type.FocusType
import com.acon.acon.feature.profile.composable.utils.isAllowedChar
import com.acon.acon.feature.profile.composable.utils.isKorean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileModViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    application: Application
) : AndroidViewModel(application), ContainerHost<ProfileModState, ProfileModSideEffect> {

    private var isInitialNicknameValidated = false

    override val container = container<ProfileModState, ProfileModSideEffect>(ProfileModState()) {
        fetchUserProfileInfo()
    }

    fun onFocusChanged(isFocused: Boolean, field: FocusType) = intent {
        reduce {
            when (field) {
                FocusType.Nickname -> state.copy(
                    nickNameFieldStatus = if (isFocused) TextFieldStatus.Focused else TextFieldStatus.Inactive
                )

                FocusType.Birthday -> state.copy(
                    birthdayFieldStatus = if (isFocused) TextFieldStatus.Focused else TextFieldStatus.Inactive
                )
            }
        }
    }

    private fun fetchUserProfileInfo() = intent {
        profileRepository.fetchProfile().onSuccess { profile ->
            val cleanedNickname = profile.nickname
            val cleanedBirthday = profile.birthDate?.filter { it.isDigit() } ?: ""

            reduce {
                state.copy(
                    selectedPhotoUri = profile.image,
                    originalPhotoUri = profile.image,
                    originalNickname = profile.nickname,
                    nickNameState = profile.nickname,
                    originalBirthday = profile.birthDate?.filter { it.isDigit() } ?: "",
                    birthdayState = profile.birthDate?.filter { it.isDigit() } ?: ""
                )
            }
            onNicknameChanged(cleanedNickname, delayValidation = true)
            onBirthdayChanged(cleanedBirthday)
        }
    }

    private var nicknameValidationJob: Job? = null

    private suspend fun validateNickname(
        nickname: String,
        errors: MutableList<NicknameErrorType>
    ): Boolean {
        return profileRepository.validateNickname(nickname)
            .map { true }
            .recover { throwable ->
                when (throwable) {
                    is ValidateNicknameError.UnsatisfiedCondition -> {
                        errors.add(NicknameErrorType.InvalidChar)
                        false
                    }

                    is ValidateNicknameError.AlreadyUsedNickname -> {
                        errors.add(NicknameErrorType.AlreadyUsed)
                        false
                    }

                    else -> {
                        false
                    }
                }
            }
            .getOrDefault(false)
    }

    fun onNicknameChanged(text: String, delayValidation: Boolean = false) = intent {
        val filteredText = text.filter { it.isAllowedChar() }

        var nicknameCount = 0
        var limitedText = ""
        for (char in filteredText) {
            val weight = if (char.isKorean()) 2 else 1
            if (nicknameCount + weight > 16) break
            limitedText += char
            nicknameCount += weight
        }

        val updatedNicknameStatus = when {
            text.isEmpty() -> NicknameStatus.Empty
            else -> NicknameStatus.Typing
        }

        val updatedFieldStatus = when {
            text.isEmpty() -> TextFieldStatus.Empty
            else -> state.nickNameFieldStatus
        }

        reduce {
            state.copy(
                nickNameState = limitedText,
                nicknameCount = nicknameCount,
                nicknameStatus = updatedNicknameStatus,
                nickNameFieldStatus = updatedFieldStatus
            )
        }

        if (filteredText.isEmpty()) return@intent
        if (delayValidation && isInitialNicknameValidated) return@intent

        nicknameValidationJob?.cancel()
        nicknameValidationJob = viewModelScope.launch {
            if (delayValidation) delay(1000L)
            else delay(500L)

            val errors = mutableListOf<NicknameErrorType>()

            val invalidCharRegex = Regex("""[!@#$%^&*()\-=+\[\]{};:'",<>/?\\|`~]""")
            val allowedCharRegex = Regex("""^[A-Za-z0-9._가-힣ㄱ-ㅎㅏ-ㅣ]*$""")

            val hasInvalidChar = invalidCharRegex.containsMatchIn(text)
            val matchesAllowedChars = allowedCharRegex.matches(text)

            if (hasInvalidChar) {
                errors.add(NicknameErrorType.InvalidChar)
            } else if (!matchesAllowedChars) {
                errors.add(NicknameErrorType.InvalidLang)
            }

            validateNickname(nickname = limitedText, errors = errors)

            intent {
                reduce {
                    state.copy(
                        nicknameStatus = when {
                            limitedText.isBlank() -> NicknameStatus.Empty
                            errors.isNotEmpty() -> NicknameStatus.Error(errors)
                            else -> NicknameStatus.Valid
                        }
                    )
                }
            }
        }
    }

    fun onBirthdayChanged(text: String) = intent {
        val digitsOnly = text.filter { it.isDigit() }

        if (digitsOnly.isEmpty()) {
            reduce {
                state.copy(
                    birthdayState = "",
                    birthdayStatus = BirthdayStatus.Empty,
                    birthdayFieldStatus = TextFieldStatus.Empty
                )
            }
            return@intent
        }

        if (digitsOnly.length <= 8) {
            reduce {
                state.copy(
                    birthdayState = digitsOnly,
                    birthdayStatus = BirthdayStatus.Invalid(errorMsg = "정확한 생년월일을 입력해주세요"),
                    birthdayFieldStatus = TextFieldStatus.Error
                )
            }
        }

        if (digitsOnly.length == 8) {
            if (validateBirthday(digitsOnly)) {
                reduce {
                    state.copy(
                        birthdayStatus = BirthdayStatus.Valid,
                        birthdayFieldStatus = TextFieldStatus.Active
                    )
                }
            } else {
                reduce {
                    state.copy(
                        birthdayStatus = BirthdayStatus.Invalid(errorMsg = "정확한 생년월일을 입력해주세요"),
                        birthdayFieldStatus = TextFieldStatus.Error
                    )
                }
            }
        }
    }

    private fun validateBirthday(birthday: String): Boolean {

        val year = birthday.substring(0, 4).toIntOrNull() ?: return false
        val month = birthday.substring(4, 6).toIntOrNull() ?: return false
        val day = birthday.substring(6, 8).toIntOrNull() ?: return false

        val currentYear = java.time.Year.now().value
        if (year > currentYear || year <= 1940) return false

        if (month !in 1..12) return false

        val maxDays = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (java.time.Year.isLeap(year.toLong())) 29 else 28
            else -> return false
        }
        if (day !in 1..maxDays) return false

        return true
    }

    fun showExitDialog() = intent {
        reduce {
            state.copy(showExitDialog = true)
        }
    }

    fun hideExitDialog() = intent {
        reduce {
            state.copy(showExitDialog = false)
        }
    }

    fun onProfileClicked() = intent {
        reduce {
            state.copy(requestPhotoPermission = true)
        }
    }

    fun onPermissionHandled() = intent {
        reduce {
            state.copy(requestPhotoPermission = false)
        }
    }

    fun showPermissionDialog() = intent {
        reduce {
            state.copy(showPermissionDialog = true)
        }
    }

    fun hidePermissionDialog() = intent {
        reduce {
            state.copy(showPermissionDialog = false)
        }
    }

    fun onPermissionSettingClick(packageName: String) = intent {
        postSideEffect(ProfileModSideEffect.NavigateToSettings(packageName))
        reduce {
            state.copy(showPermissionDialog = false)
        }
    }

    fun updateProfileImage(selectedPhotoUri: String) = intent {
        reduce {
            state.copy(selectedPhotoUri = selectedPhotoUri)
        }
    }

    fun showProfileEditDialog() = intent {
        reduce {
            state.copy(showPhotoEditDialog = true)
        }
    }

    fun hideProfileEditDialog() = intent {
        reduce {
            state.copy(showPhotoEditDialog = false)
        }
    }

    fun getPreSignedUrl() = intent {

        if (state.selectedPhotoUri == "") {
            if (state.birthdayStatus == BirthdayStatus.Valid) {
                updateProfile(
                    fileName = state.uploadFileName,
                    nickname = state.nickNameState,
                    birthday = state.birthdayState
                )
            } else {
                updateProfile(
                    fileName = state.uploadFileName,
                    nickname = state.nickNameState,
                    birthday = null
                )
            }
        } else {
            viewModelScope.launch {
                profileRepository.getPreSignedUrl()
                    .onSuccess { result ->
                        reduce {
                            state.copy(
                                uploadFileName = result.fileName,
                                preSignedUrl = result.preSignedUrl
                            )
                        }
                        putPhotoToPreSignedUrl(
                            Uri.parse(state.selectedPhotoUri),
                            state.preSignedUrl
                        )
                    }
                    .onFailure {
                        // 실패시 에러 처리
                    }
            }
        }
    }

    private fun putPhotoToPreSignedUrl(imageUri: Uri, preSignedUrl: String) = intent {
        val context = getApplication<Application>().applicationContext
        val client = OkHttpClient()

        try {
            val byteArray: ByteArray
            val mimeType: String

            if (state.selectedPhotoUri.startsWith("content://")) {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                byteArray = inputStream?.readBytes()
                    ?: throw IllegalArgumentException("Failed to read image")
                mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"

            } else if (state.selectedPhotoUri.startsWith("http://") || state.selectedPhotoUri.startsWith(
                    "https://"
                )
            ) {
                val getRequest = Request.Builder().url(imageUri.toString()).build()
                val getResponse = client.newCall(getRequest).execute()

                if (!getResponse.isSuccessful) {
                    throw IllegalArgumentException("Failed to fetch remote image")
                }

                byteArray = getResponse.body?.bytes()
                    ?: throw IllegalArgumentException("Failed to read remote image")
                mimeType = getResponse.header("Content-Type") ?: "image/jpeg"

            } else {
                throw IllegalArgumentException("Unsupported URI scheme")
            }

            val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), byteArray)

            val request = Request.Builder()
                .url(preSignedUrl)
                .put(fileBody)
                .addHeader("Content-Type", mimeType)
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                if (state.birthdayStatus == BirthdayStatus.Valid) {
                    updateProfile(
                        fileName = state.uploadFileName,
                        nickname = state.nickNameState,
                        birthday = state.birthdayState
                    )
                } else {
                    updateProfile(
                        fileName = state.uploadFileName,
                        nickname = state.nickNameState,
                        birthday = null
                    )
                }
            } else {
                // PUT 실패 시 에러 처리
            }
        } catch (e: Exception) {
        }
    }

    private fun updateProfile(fileName: String, nickname: String, birthday: String?) = intent {
        profileRepository.updateProfile(fileName, nickname, birthday)
            .onSuccess {
                intent {
                    postSideEffect(ProfileModSideEffect.NavigateToProfile)
                }
            }
            .onFailure {
                intent {
                    postSideEffect(ProfileModSideEffect.NavigateToProfile)
                }
            }
    }

}

data class ProfileModState(
    val originalNickname: String = "",
    val nickNameFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
    val nickNameState: String = "",
    val nicknameStatus: NicknameStatus = NicknameStatus.Empty,
    val nicknameCount: Int = 0,

    val originalBirthday: String = "",
    val birthdayFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
    val birthdayState: String = "",
    val birthdayStatus: BirthdayStatus = BirthdayStatus.Empty,

    val showExitDialog: Boolean = false,

    val requestPhotoPermission: Boolean = false,
    val showPermissionDialog: Boolean = false,

    val originalPhotoUri: String = "",
    val selectedPhotoUri: String = "",
    val showPhotoEditDialog: Boolean = false,

    val preSignedUrl: String = "",
    val uploadFileName: String = "",
)

sealed class NicknameErrorType(val message: String) {
    data object InvalidChar : NicknameErrorType("._ 이외의 특수기호는 사용할 수 없어요")
    data object InvalidLang : NicknameErrorType("한국어, 영어 이외의 언어는 사용할 수 없어요")
    data object AlreadyUsed : NicknameErrorType("이미 사용 중인 닉네임이에요")
}

sealed class NicknameStatus {
    data object Empty : NicknameStatus()
    data object Typing : NicknameStatus()
    data object Valid : NicknameStatus()
    data class Error(val errorTypes: List<NicknameErrorType>) : NicknameStatus()
}

sealed class BirthdayStatus {
    data object Empty : BirthdayStatus()
    data class Invalid(val errorMsg: String?) : BirthdayStatus()
    data object Valid : BirthdayStatus()
}

sealed interface ProfileModSideEffect {
    data object NavigateBack : ProfileModSideEffect
    data class NavigateToSettings(val packageName: String) : ProfileModSideEffect
    data object NavigateToCustomGallery : ProfileModSideEffect
    data class UpdateProfileImage(val imageUri: String?) : ProfileModSideEffect
    data object NavigateToProfile : ProfileModSideEffect
}