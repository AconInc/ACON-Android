package com.acon.acon.feature.profile.composable.screen.profileMod

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.acon.acon.domain.error.profile.ValidateNicknameError
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.type.UpdateProfileType
import com.acon.acon.feature.profile.composable.type.BirthdayValidationStatus
import com.acon.acon.feature.profile.composable.type.FocusType
import com.acon.acon.feature.profile.composable.type.NicknameErrorType
import com.acon.acon.feature.profile.composable.type.NicknameValidationStatus
import com.acon.acon.feature.profile.composable.type.TextFieldStatus
import com.acon.acon.feature.profile.composable.utils.limitedNickname
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ProfileModViewModelV2 @Inject constructor(
    private val profileRepository: ProfileRepository,
    application: Application
) : AndroidViewModel(application), ContainerHost<ProfileModStateV2, ProfileModSideEffectV2> {

    private var nicknameValidationJob: Job? = null

    override val container = container<ProfileModStateV2, ProfileModSideEffectV2>(ProfileModStateV2.Loading) {
        fetchUserProfileInfo()
    }

    fun onFocusChanged(isFocused: Boolean, field: FocusType) = intent {
        runOn<ProfileModStateV2.Success> {
            reduce {
                when (field) {
                    FocusType.Nickname -> state.copy(
                        nicknameFieldStatus = if (isFocused) TextFieldStatus.Focused else TextFieldStatus.Inactive
                    )

                    FocusType.Birthday -> state.copy(
                        birthdayFieldStatus = if (isFocused) TextFieldStatus.Focused else TextFieldStatus.Inactive
                    )
                }
            }
        }
    }

    private fun fetchUserProfileInfo() = intent {
        profileRepository.fetchProfile().onSuccess { profile ->
            val cleanedBirthday = profile.birthDate?.filter { it.isDigit() } ?: ""
            reduce {
                ProfileModStateV2.Success(
                    fetchedNickname = profile.nickname,
                    fetchedBirthday = cleanedBirthday,
                    birthdayTextFieldValue = TextFieldValue(cleanedBirthday),
                    fetchedPhotoUri = profile.image,
                )
            }
            onNicknameChanged(profile.nickname, delayValidation = true)
            onBirthdayChanged(TextFieldValue(cleanedBirthday))
        }
    }

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
        runOn<ProfileModStateV2.Success> {
            val (limitedText, count) = text.limitedNickname()

            val invalidCharRegex = Regex("""[!@#$%^&*()\-=+\[\]{};:'",<>/?\\|`~]""")
            val allowedCharRegex = Regex("""^[A-Za-z0-9._가-힣ㄱ-ㅎㅏ-ㅣ]*$""")
            val hasInvalidChar = invalidCharRegex.containsMatchIn(limitedText)
            val matchesAllowedChars = allowedCharRegex.matches(limitedText)

            val errors = mutableListOf<NicknameErrorType>()
            if (hasInvalidChar) errors.add(NicknameErrorType.InvalidChar)
            else if (!matchesAllowedChars) errors.add(NicknameErrorType.InvalidLang)

            reduce {
                state.copy(
                    isEdited = (state.isEdited || limitedText != state.fetchedNickname),
                    nicknameCount = count,
                    nicknameValidationStatus = if (limitedText.isBlank()) NicknameValidationStatus.Empty else NicknameValidationStatus.Typing,
                    nicknameFieldStatus = if (limitedText.isEmpty()) TextFieldStatus.Empty else state.nicknameFieldStatus
                )
            }
            nicknameValidationJob?.cancel()
            nicknameValidationJob = viewModelScope.launch {
                if (delayValidation) delay(1000L) else delay(500L)

                val serverErrors = mutableListOf<NicknameErrorType>()
                val isValid = validateNickname(limitedText, serverErrors)

                reduce {
                    state.copy(
                        nicknameValidationStatus = when {
                            limitedText.isBlank() -> NicknameValidationStatus.Empty
                            serverErrors.isNotEmpty() -> NicknameValidationStatus.Error(serverErrors)
                            errors.isNotEmpty() -> NicknameValidationStatus.Error(errors)
                            isValid -> NicknameValidationStatus.Valid
                            else -> NicknameValidationStatus.Typing
                        }
                    )
                }
            }
        }
    }

    fun onBirthdayChanged(fieldValue: TextFieldValue) = intent {
        runOn<ProfileModStateV2.Success> {
            val digitsOnly = fieldValue.text.filter { it.isDigit() }

            val limitedDigits = digitsOnly.take(8)
            val newSelection = TextRange(limitedDigits.length)
            val newTextFieldValue = TextFieldValue(
                text = limitedDigits,
                selection = newSelection
            )

            when {
                limitedDigits.isEmpty() -> {
                    reduce {
                        state.copy(
                            isEdited = (state.isEdited || newTextFieldValue.text != state.fetchedBirthday),
                            birthdayTextFieldValue = newTextFieldValue,
                            birthdayValidationStatus = BirthdayValidationStatus.Empty,
                            birthdayFieldStatus = TextFieldStatus.Empty
                        )
                    }
                }
                limitedDigits.length < 8 -> {
                    reduce {
                        state.copy(
                            isEdited = (state.isEdited || newTextFieldValue.text != state.fetchedBirthday),
                            birthdayTextFieldValue = newTextFieldValue,
                            birthdayValidationStatus = BirthdayValidationStatus.Invalid(errorMsg = "정확한 생년월일을 입력해주세요"),
                            birthdayFieldStatus = TextFieldStatus.Error
                        )
                    }
                }
                limitedDigits.length == 8 -> {
                    val isValid = validateBirthday(limitedDigits)
                    reduce {
                        state.copy(
                            isEdited = (state.isEdited || newTextFieldValue.text != state.fetchedBirthday),
                            birthdayTextFieldValue = newTextFieldValue,
                            birthdayValidationStatus = if (isValid) BirthdayValidationStatus.Valid
                            else BirthdayValidationStatus.Invalid(errorMsg = "정확한 생년월일을 입력해주세요"),
                            birthdayFieldStatus = if (isValid) TextFieldStatus.Active else TextFieldStatus.Error
                        )
                    }
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
        return day in 1..maxDays
    }

    fun navigateToCustomGallery() = intent {
        postSideEffect(ProfileModSideEffectV2.NavigateToCustomGallery)
    }

    fun navigateToBack() = intent {
        postSideEffect(ProfileModSideEffectV2.NavigateBack)
    }

    fun onRequestExitDialog() = intent {
        runOn<ProfileModStateV2.Success> {
            reduce {
                state.copy(showExitDialog = true)
            }
        }
    }

    fun onDisMissExitDialog() = intent {
        runOn<ProfileModStateV2.Success> {
            reduce {
                state.copy(showExitDialog = false)
            }
        }
    }

    fun onRequestPhotoPermission() = intent {
        runOn<ProfileModStateV2.Success> {
            reduce {
                state.copy(requestPhotoPermission = true)
            }
        }
    }

    fun onDisMissPhotoPermission() = intent {
        runOn<ProfileModStateV2.Success> {
            reduce {
                state.copy(requestPhotoPermission = false)
            }
        }
    }

    fun onRequestPermissionDialog() = intent {
        runOn<ProfileModStateV2.Success> {
            reduce {
                state.copy(showPermissionDialog = true)
            }
        }
    }

    fun onDisMissPermissionDialog() = intent {
        runOn<ProfileModStateV2.Success> {
            reduce {
                state.copy(showPermissionDialog = false)
            }
        }
    }

    fun onPermissionSettingClick(packageName: String) = intent {
        runOn<ProfileModStateV2.Success> {
            postSideEffect(ProfileModSideEffectV2.NavigateToSettings(packageName))
            reduce {
                state.copy(showPermissionDialog = false)
            }
        }
    }

    fun updateProfileImage(selectedPhotoUri: String) = intent {
        runOn<ProfileModStateV2.Success> {
            reduce {
                state.copy(selectedPhotoUri = selectedPhotoUri)
            }
        }
    }

    fun onRequestProfileEditModal() = intent {
        runOn<ProfileModStateV2.Success> {
            reduce {
                state.copy(showPhotoEditModal = true)
            }
        }
    }

    fun onDisMissProfileEditModal() = intent {
        runOn<ProfileModStateV2.Success> {
            reduce {
                state.copy(showPhotoEditModal = false)
            }
        }
    }

    fun getPreSignedUrl(nickname: String) = intent {
        runOn<ProfileModStateV2.Success> {
            if (state.selectedPhotoUri == "basic_profile_image") {
                if (state.birthdayValidationStatus == BirthdayValidationStatus.Valid) {
                    updateProfile(
                        fileName = state.uploadFileName,
                        nickname = nickname,
                        birthday = state.birthdayTextFieldValue.text
                    )
                } else {
                    updateProfile(
                        fileName = state.uploadFileName,
                        nickname = nickname,
                        birthday = null
                    )
                }
            } else {
                profileRepository.getPreSignedUrl()
                    .onSuccess { result ->
                        reduce {
                            state.copy(
                                uploadFileName = result.fileName
                            )
                        }
                        putPhotoToPreSignedUrl(
                            nickname = nickname,
                            imageUri = Uri.parse(state.selectedPhotoUri),
                            preSignedUrl = result.preSignedUrl
                        )
                    }
                    .onFailure {
                        // 실패시 에러 처리
                    }
            }
        }
    }

    private fun putPhotoToPreSignedUrl(nickname: String, imageUri: Uri, preSignedUrl: String) = intent {
        runOn<ProfileModStateV2.Success> {
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
                    if (state.birthdayValidationStatus == BirthdayValidationStatus.Valid) {
                        updateProfile(
                            fileName = state.uploadFileName,
                            nickname = nickname,
                            birthday = state.birthdayTextFieldValue.text
                        )
                    } else {
                        updateProfile(
                            fileName = state.uploadFileName,
                            nickname = nickname,
                            birthday = null
                        )
                    }
                } else {
                    // PUT 실패 시 에러 처리
                }
            } catch (e: Exception) { }
        }
    }

    private fun updateProfile(fileName: String, nickname: String, birthday: String?) = intent {
        profileRepository.updateProfile(fileName, nickname, birthday)
            .onSuccess {
                profileRepository.updateProfileType(UpdateProfileType.SUCCESS)
                postSideEffect(ProfileModSideEffectV2.NavigateToProfile)
            }
            .onFailure {
                profileRepository.updateProfileType(UpdateProfileType.FAILURE)
                postSideEffect(ProfileModSideEffectV2.NavigateToProfile)
            }
    }
}

sealed interface ProfileModStateV2 {
    @Immutable
    data class Success(
        val fetchedNickname: String = "",
        //val nicknameTextFieldValue: TextFieldValue = TextFieldValue(),
        val nicknameFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
        val nicknameValidationStatus: NicknameValidationStatus = NicknameValidationStatus.Empty,
        val nicknameCount: Int = 0,

        val fetchedBirthday: String = "",
        val birthdayTextFieldValue: TextFieldValue = TextFieldValue(),
        val birthdayFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
        val birthdayValidationStatus: BirthdayValidationStatus = BirthdayValidationStatus.Empty,

        val fetchedPhotoUri: String = "",
        val selectedPhotoUri: String = "",
        val uploadFileName: String = "",

        val isEdited: Boolean = false,
        val showExitDialog: Boolean = false,
        val requestPhotoPermission: Boolean = false,
        val showPermissionDialog: Boolean = false,
        val showPhotoEditModal: Boolean = false,
    ): ProfileModStateV2

    data object Loading : ProfileModStateV2
    data object LoadFailed : ProfileModStateV2
}

sealed interface ProfileModSideEffectV2 {
    data object NavigateBack : ProfileModSideEffectV2
    data class NavigateToSettings(val packageName: String) : ProfileModSideEffectV2
    data object NavigateToCustomGallery : ProfileModSideEffectV2
    data class UpdateProfileImage(val imageUri: String?) : ProfileModSideEffectV2
    data object NavigateToProfile : ProfileModSideEffectV2
}