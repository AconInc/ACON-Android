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
import com.acon.acon.feature.profile.composable.utils.isAllowedChar
import com.acon.acon.feature.profile.composable.utils.limitedNickname
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ProfileModViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    application: Application
) : AndroidViewModel(application), ContainerHost<ProfileModState, ProfileModSideEffect> {

    private var nicknameValidationJob: Job? = null

    override val container =
        container<ProfileModState, ProfileModSideEffect>(ProfileModState.Loading) {
            fetchUserProfileInfo()
        }

    fun onFocusChanged(isFocused: Boolean, field: FocusType) = intent {
        runOn<ProfileModState.Success> {
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
            val limitedNickname = profile.nickname.lowercase().take(14)

            reduce {
                ProfileModState.Success(
                    fetchedNickname = limitedNickname,
                    fetchedBirthday = cleanedBirthday,
                    birthdayTextFieldValue = TextFieldValue(cleanedBirthday),
                    fetchedPhotoUri = profile.image,
                )
            }
            onNicknameChanged(limitedNickname, delayValidation = true)
            onBirthdayChanged(TextFieldValue(cleanedBirthday))
        }
    }

    private suspend fun validateNickname(
        nickname: String
    ): NicknameErrorType? {
        return profileRepository.validateNickname(nickname)
            .map { null }
            .recover { throwable ->
                when (throwable) {
                    is ValidateNicknameError.UnsatisfiedCondition -> NicknameErrorType.Invalid
                    is ValidateNicknameError.AlreadyUsedNickname -> NicknameErrorType.Duplicate
                    else -> null
                }
            }
            .getOrNull()
    }

    fun onNicknameChanged(text: String, delayValidation: Boolean = false) = intent {
        runOn<ProfileModState.Success> {
            val (limitedText, count) = text.limitedNickname()

            val localErrorType: NicknameErrorType? = when {
                text.any { !it.isAllowedChar() } -> NicknameErrorType.Invalid
                else -> null
            }

            reduce {
                state.copy(
                    nickname = limitedText,
                    isEdited = (state.isEdited || limitedText != state.fetchedNickname),
                    nicknameCount = count,
                    nicknameValidationStatus = when {
                        text.isEmpty() -> NicknameValidationStatus.Empty
                        localErrorType != null -> NicknameValidationStatus.Error(localErrorType)
                        else -> NicknameValidationStatus.Typing
                    },
                    nicknameFieldStatus = if (limitedText.isEmpty()) TextFieldStatus.Empty else state.nicknameFieldStatus
                )
            }

            nicknameValidationJob?.cancel()
            nicknameValidationJob = viewModelScope.launch {
                if (text.length > 14) return@launch
                if (delayValidation) delay(1000L) else delay(500L)

                if (localErrorType == NicknameErrorType.Invalid) return@launch
                val serverErrorType = validateNickname(limitedText)

                reduce {
                    state.copy(
                        nicknameValidationStatus = when {
                            limitedText.isBlank() -> NicknameValidationStatus.Empty
                            serverErrorType != null -> NicknameValidationStatus.Error(
                                serverErrorType
                            )

                            localErrorType != null -> NicknameValidationStatus.Error(localErrorType)
                            else -> NicknameValidationStatus.Valid
                        }
                    )
                }
            }
        }
    }

    fun onBirthdayChanged(fieldValue: TextFieldValue) = intent {
        runOn<ProfileModState.Success> {
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
                            birthdayValidationStatus = BirthdayValidationStatus.Invalid,
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
                            else BirthdayValidationStatus.Invalid,
                            birthdayFieldStatus = if (isValid) TextFieldStatus.Focused else TextFieldStatus.Error
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
        return try {
            val inputDate = java.time.LocalDate.of(year, month, day)
            val today = java.time.LocalDate.now()
            !inputDate.isAfter(today)
        } catch (e: Exception) {
            false
        }
    }

    fun navigateToCustomGallery() = intent {
        postSideEffect(ProfileModSideEffect.NavigateToCustomGallery)
    }

    fun navigateToBack() = intent {
        postSideEffect(ProfileModSideEffect.NavigateBack)
    }

    fun onRequestExitDialog() = intent {
        runOn<ProfileModState.Success> {
            reduce {
                state.copy(showExitDialog = true)
            }
        }
    }

    fun onDisMissExitDialog() = intent {
        runOn<ProfileModState.Success> {
            reduce {
                state.copy(showExitDialog = false)
            }
        }
    }

    fun onRequestPhotoPermission() = intent {
        runOn<ProfileModState.Success> {
            reduce {
                state.copy(requestPhotoPermission = true)
            }
        }
    }

    fun onPhotoPermissionDenied() = intent {
        runOn<ProfileModState.Success> {
            reduce {
                state.copy(requestPhotoPermission = false)
            }
        }
    }

    fun onRequestPermissionDialog() = intent {
        runOn<ProfileModState.Success> {
            reduce {
                state.copy(showPermissionDialog = true)
            }
        }
    }

    fun onDisMissPermissionDialog() = intent {
        runOn<ProfileModState.Success> {
            reduce {
                state.copy(showPermissionDialog = false)
            }
        }
    }

    fun onPermissionSettingClick(packageName: String) = intent {
        runOn<ProfileModState.Success> {
            postSideEffect(ProfileModSideEffect.NavigateToSettings(packageName))
            reduce {
                state.copy(showPermissionDialog = false)
            }
        }
    }

    fun updateProfileImage(selectedPhotoUri: String) = intent {
        runOn<ProfileModState.Success> {
            reduce {
                state.copy(selectedPhotoUri = selectedPhotoUri)
            }
        }
    }

    fun onRequestProfileEditModal() = intent {
        runOn<ProfileModState.Success> {
            reduce {
                state.copy(showPhotoEditModal = true)
            }
        }
    }

    fun onDisMissProfileEditModal() = intent {
        runOn<ProfileModState.Success> {
            reduce {
                state.copy(showPhotoEditModal = false)
            }
        }
    }

    fun getPreSignedUrl() = intent {
        runOn<ProfileModState.Success> {
            val nickname = state.nickname
            val isBirthdayValid = state.birthdayValidationStatus == BirthdayValidationStatus.Valid
            val birthday = if (isBirthdayValid) state.birthdayTextFieldValue.text else null

            when {
                state.selectedPhotoUri.isEmpty() -> {
                    updateProfile(
                        fileName = state.fetchedPhotoUri,
                        nickname = nickname,
                        birthday = birthday
                    )
                }

                state.selectedPhotoUri == "basic_profile_image" -> {
                    updateProfile(
                        fileName = state.uploadFileName,
                        nickname = nickname,
                        birthday = birthday
                    )
                }

                else -> {
                    profileRepository.getPreSignedUrl()
                        .onSuccess { result ->
                            reduce { state.copy(uploadFileName = result.fileName) }
                            putPhotoToPreSignedUrl(
                                nickname = nickname,
                                imageUri = Uri.parse(state.selectedPhotoUri),
                                preSignedUrl = result.preSignedUrl
                            )
                        }
                        .onFailure {
                            // 실패 처리
                        }
                }
            }
        }
    }

    private fun putPhotoToPreSignedUrl(nickname: String, imageUri: Uri, preSignedUrl: String) =
        intent {
            runOn<ProfileModState.Success> {
                val context = getApplication<Application>().applicationContext
                val client = OkHttpClient()
                Timber.tag(TAG).d("imageUri: $imageUri")

                try {
                    val byteArray: ByteArray
                    val mimeType: String

                    if (state.selectedPhotoUri.startsWith("content://")) {
                        val inputStream = context.contentResolver.openInputStream(imageUri)
                        byteArray = inputStream?.readBytes()
                            ?: throw IllegalArgumentException("Failed to read image")
                        mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"

                    } else if (state.selectedPhotoUri.startsWith("http://") ||
                        state.selectedPhotoUri.startsWith("https://")
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

                    val fileBody =
                        byteArray.toRequestBody(mimeType.toMediaTypeOrNull(), 0, byteArray.size)

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
                } catch (e: Exception) {
                    Timber.tag(TAG).e(e, "이미지 업로드 과정에서 예외 발생: ${e.message}")
                }
            }
        }

    private fun updateProfile(fileName: String, nickname: String, birthday: String?) = intent {
        profileRepository.updateProfile(fileName, nickname, birthday)
            .onSuccess {
                profileRepository.updateProfileType(UpdateProfileType.SUCCESS)
                postSideEffect(ProfileModSideEffect.NavigateToProfile)
            }
            .onFailure {
                profileRepository.updateProfileType(UpdateProfileType.FAILURE)
                postSideEffect(ProfileModSideEffect.NavigateToProfile)
            }
    }

    companion object {
        const val TAG = "ProfileViewModel"
    }
}

sealed interface ProfileModState {
    @Immutable
    data class Success(
        val fetchedNickname: String = "",
        val nickname: String = "",
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
    ) : ProfileModState {
        val isEditButtonEnabled: Boolean
            get() {
                val isProfileImageChanged = when {
                    selectedPhotoUri.contains("basic_profile_image") &&
                            fetchedPhotoUri.contains("basic_profile_image") -> false

                    selectedPhotoUri.isNotEmpty() && selectedPhotoUri != fetchedPhotoUri -> true
                    else -> false
                }
                val isBirthValid =
                    fetchedBirthday.isNotEmpty() && birthdayTextFieldValue.text.isEmpty()
                val isContentValid = nicknameValidationStatus == NicknameValidationStatus.Valid &&
                        (birthdayTextFieldValue.text.isEmpty() ||
                                birthdayValidationStatus == BirthdayValidationStatus.Valid)

                return isProfileImageChanged || isBirthValid || (isEdited && isContentValid)
            }
    }

    data object Loading : ProfileModState
    data object LoadFailed : ProfileModState
}

sealed interface ProfileModSideEffect {
    data object NavigateBack : ProfileModSideEffect
    data class NavigateToSettings(val packageName: String) : ProfileModSideEffect
    data object NavigateToCustomGallery : ProfileModSideEffect
    data class UpdateProfileImage(val imageUri: String?) : ProfileModSideEffect
    data object NavigateToProfile : ProfileModSideEffect
}