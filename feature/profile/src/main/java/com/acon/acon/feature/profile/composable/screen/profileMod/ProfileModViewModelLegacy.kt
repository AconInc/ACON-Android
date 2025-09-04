package com.acon.acon.feature.profile.composable.screen.profileMod

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.acon.acon.domain.error.profile.ValidateNicknameErrorLegacy
import com.acon.acon.domain.repository.ProfileRepositoryLegacy
import com.acon.acon.feature.profile.BuildConfig
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
class ProfileModViewModelLegacy @Inject constructor(
    private val profileRepositoryLegacy: ProfileRepositoryLegacy,
    application: Application
) : AndroidViewModel(application), ContainerHost<ProfileModStateLegacy, ProfileModSideEffectLegacy> {

    private var nicknameValidationJob: Job? = null

    override val container =
        container<ProfileModStateLegacy, ProfileModSideEffectLegacy>(ProfileModStateLegacy.Loading) {
            fetchUserProfileInfo()
        }

    fun onFocusChanged(isFocused: Boolean, field: FocusType) = intent {
        runOn<ProfileModStateLegacy.Success> {
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
        profileRepositoryLegacy.fetchProfile().collect {
            it.onSuccess { profile ->
                reduce {
                    ProfileModStateLegacy.Success(
                        fetchedNickname = profile.nickname,
                        fetchedBirthday = profile.birthDate?.filter { it.isDigit() } ?: "",
                        fetchedPhotoUri = profile.image
                    )
                }
                onNicknameChanged(profile.nickname, delayValidation = true)
                onBirthdayChanged(profile.birthDate ?: "")
            }
        }
    }

    private suspend fun validateNickname(
        nickname: String
    ): NicknameErrorType? {
        return profileRepositoryLegacy.validateNickname(nickname)
            .map { null }
            .recover { throwable ->
                when (throwable) {
                    is ValidateNicknameErrorLegacy.UnsatisfiedCondition -> NicknameErrorType.Invalid
                    is ValidateNicknameErrorLegacy.AlreadyUsedNickname -> NicknameErrorType.Duplicate
                    else -> null
                }
            }
            .getOrNull()
    }

    fun onNicknameChanged(text: String, delayValidation: Boolean = false) = intent {
        runOn<ProfileModStateLegacy.Success> {
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

    fun onBirthdayChanged(text: String) = intent {
        runOn<ProfileModStateLegacy.Success> {
            val digitsOnly = text.filter { it.isDigit() }
            val limitedDigits = digitsOnly.take(8)

            val (validationStatus, fieldStatus) = when {
                limitedDigits.isEmpty() -> BirthdayValidationStatus.Empty to TextFieldStatus.Empty
                limitedDigits.length < 8 -> BirthdayValidationStatus.Empty to TextFieldStatus.Focused
                else -> {
                    val isValid = validateBirthday(limitedDigits)
                    (if (isValid) BirthdayValidationStatus.Valid else BirthdayValidationStatus.Invalid) to
                            (if (isValid) TextFieldStatus.Focused else TextFieldStatus.Error)
                }
            }

            reduce {
                state.copy(
                    isEdited = state.isEdited || limitedDigits != state.fetchedBirthday,
                    birthday = limitedDigits,
                    birthdayValidationStatus = validationStatus,
                    birthdayFieldStatus = fieldStatus
                )
            }
        }
    }

    private fun validateBirthday(birthday: String): Boolean {
        val today = java.time.LocalDate.now()
        val year = birthday.substring(0, 4).toIntOrNull() ?: return false
        val month = birthday.substring(4, 6).toIntOrNull() ?: return false
        val day = birthday.substring(6, 8).toIntOrNull() ?: return false

        val currentYear = java.time.Year.now().value
        if (year > currentYear || year <= 1940) return false

        if (month !in 1..12) return false

        val maxDays = java.time.YearMonth.of(year, month).lengthOfMonth()
        if (day !in 1..maxDays) return false

        return try {
            val inputDate = java.time.LocalDate.of(year, month, day)
            !inputDate.isAfter(today)
        } catch (e: Exception) {
            false
        }
    }

    fun navigateToBack() = intent {
        postSideEffect(ProfileModSideEffectLegacy.NavigateBack)
    }

    fun onRequestExitDialog() = intent {
        runOn<ProfileModStateLegacy.Success> {
            reduce {
                state.copy(showExitDialog = true)
            }
        }
    }

    fun onDisMissExitDialog() = intent {
        runOn<ProfileModStateLegacy.Success> {
            reduce {
                state.copy(showExitDialog = false)
            }
        }
    }

    fun updateProfileImage(selectedPhotoUri: String) = intent {
        runOn<ProfileModStateLegacy.Success> {
            reduce {
                state.copy(selectedPhotoUri = selectedPhotoUri)
            }
        }
    }

    fun onRequestProfileEditModal() = intent {
        runOn<ProfileModStateLegacy.Success> {
            reduce {
                state.copy(showPhotoEditModal = true)
            }
        }
    }

    fun onDisMissProfileEditModal() = intent {
        runOn<ProfileModStateLegacy.Success> {
            reduce {
                state.copy(showPhotoEditModal = false)
            }
        }
    }

    fun getPreSignedUrl() = intent {
        runOn<ProfileModStateLegacy.Success> {
            Timber.tag(TAG).d("getPreSignedUrl() 호출됨")
            val nickname = state.nickname
            val isBirthdayValid = state.birthdayValidationStatus == BirthdayValidationStatus.Valid
            val birthday = if (isBirthdayValid) state.birthday else null

            when {
                state.selectedPhotoUri.isEmpty() -> {
                    Timber.tag(TAG).d("selectedPhotoUri가 비어 있음 → 기존 이미지로 updateProfile 호출")
                    updateProfile(
                        fileName = state.fetchedPhotoUri,
                        nickname = nickname,
                        birthday = birthday,
                        uri = state.fetchedPhotoUri
                    )
                }

                state.selectedPhotoUri == "basic_profile_image" -> {
                    Timber.tag(TAG).d("selectedPhotoUri가 basic_profile_image → 기본 이미지로 updateProfile 호출")
                    updateProfile(
                        fileName = state.uploadFileName,
                        nickname = nickname,
                        birthday = birthday,
                        uri = state.selectedPhotoUri
                    )
                }

                else -> {
                    Timber.tag(TAG).d("selectedPhotoUri가 커스텀 이미지 → presigned URL 요청")
                    profileRepositoryLegacy.getPreSignedUrl()
                        .onSuccess { result ->
                            reduce { state.copy(uploadFileName = result.fileName) }
                            putPhotoToPreSignedUrl(
                                nickname = nickname,
                                birthday = birthday,
                                imageUri = Uri.parse(state.selectedPhotoUri),
                                preSignedUrl = result.preSignedUrl
                            )
                        }
                        .onFailure {
                            Timber.tag(TAG).e(it, "presigned URL 획득 실패")
                        }
                }
            }
        }
    }

    private fun putPhotoToPreSignedUrl(
        nickname: String,
        birthday: String?,
        imageUri: Uri,
        preSignedUrl: String
    ) = intent {
        runOn<ProfileModStateLegacy.Success> {
            val context = getApplication<Application>().applicationContext
            val client = OkHttpClient()

            try {
                val byteArray: ByteArray
                val mimeType: String

                if (state.selectedPhotoUri.startsWith("content://")) {
                    val inputStream = context.contentResolver.openInputStream(imageUri)
                    byteArray = inputStream?.readBytes()
                        ?: throw IllegalArgumentException("이미지 읽기 실패")
                    mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"

                } else if (state.selectedPhotoUri.startsWith("http://") || state.selectedPhotoUri.startsWith("https://")) {
                    Timber.tag(TAG).d("원격 URL에서 이미지 가져오기 시작")
                    val getRequest = Request.Builder().url(imageUri.toString()).build()
                    val getResponse = client.newCall(getRequest).execute()

                    if (!getResponse.isSuccessful) {
                        Timber.tag(TAG).e("원격 이미지 가져오기 실패, code: %d", getResponse.code)
                        throw IllegalArgumentException("원격 이미지 가져오기 실패")
                    }

                    byteArray = getResponse.body?.bytes()
                        ?: throw IllegalArgumentException("원격 이미지 읽기 실패")
                    mimeType = getResponse.header("Content-Type") ?: "image/jpeg"

                } else {
                    Timber.tag(TAG).e("지원하지 않는 URI scheme: %s", state.selectedPhotoUri)
                    throw IllegalArgumentException("지원하지 않는 URI scheme")
                }

                val fileBody =
                    byteArray.toRequestBody(mimeType.toMediaTypeOrNull(), 0, byteArray.size)

                val request = Request.Builder()
                    .url(preSignedUrl)
                    .put(fileBody)
                    .addHeader("Content-Type", mimeType)
                    .build()

                val response = client.newCall(request).execute()
                val bucketImageUri = "${BuildConfig.BUCKET_URL}${state.uploadFileName}"

                if (response.isSuccessful) {
                    Timber.tag(TAG).d("이미지 업로드 성공")
                    if (state.birthdayValidationStatus == BirthdayValidationStatus.Valid) {
                        updateProfile(
                            fileName = state.uploadFileName,
                            nickname = nickname,
                            birthday = birthday,
                            uri = bucketImageUri
                        )
                    } else {
                        updateProfile(
                            fileName = state.uploadFileName,
                            nickname = nickname,
                            birthday = null,
                            uri = bucketImageUri
                        )
                    }
                } else {
                    Timber.tag(TAG).e("이미지 업로드 실패, code: %d", response.code)
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "이미지 업로드 과정에서 예외 발생: %s", e.message)
            }
        }
    }

    private fun updateProfile(fileName: String, nickname: String, birthday: String?, uri: String) =
        intent {
            profileRepositoryLegacy.updateProfile(fileName, nickname, birthday, uri)
                .onSuccess {
                    profileRepositoryLegacy.updateProfileType(com.acon.acon.core.model.type.UpdateProfileType.SUCCESS)
                    postSideEffect(ProfileModSideEffectLegacy.NavigateToProfileLegacy)
                }
                .onFailure {
                    profileRepositoryLegacy.updateProfileType(com.acon.acon.core.model.type.UpdateProfileType.FAILURE)
                    postSideEffect(ProfileModSideEffectLegacy.NavigateToProfileLegacy)
                }
        }

    companion object {
        const val TAG = "ProfileViewModel"
    }
}

sealed interface ProfileModStateLegacy {
    @Immutable
    data class Success(
        val fetchedNickname: String = "",
        val nickname: String = "",
        val nicknameFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
        val nicknameValidationStatus: NicknameValidationStatus = NicknameValidationStatus.Empty,
        val nicknameCount: Int = 0,

        val fetchedBirthday: String = "",
        val birthday: String = "",
        val birthdayFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
        val birthdayValidationStatus: BirthdayValidationStatus = BirthdayValidationStatus.Empty,

        val fetchedPhotoUri: String = "",
        val selectedPhotoUri: String = "",
        val uploadFileName: String = "",

        val isEdited: Boolean = false,
        val showExitDialog: Boolean = false,
        val showPhotoEditModal: Boolean = false,
    ) : ProfileModStateLegacy {
        val isEditButtonEnabled: Boolean
            get() {
                val isProfileImageChanged = when {
                    selectedPhotoUri.contains("basic_profile_image") &&
                            fetchedPhotoUri.contains("basic_profile_image") -> false

                    selectedPhotoUri.isNotEmpty() && selectedPhotoUri != fetchedPhotoUri -> true
                    else -> false
                }
                val isBirthValid = fetchedBirthday.isNotEmpty() && birthday.isEmpty()
                val isContentValid = nicknameValidationStatus == NicknameValidationStatus.Valid &&
                        (birthday.isEmpty() || birthdayValidationStatus == BirthdayValidationStatus.Valid)

                return (isProfileImageChanged && isContentValid) || isBirthValid || (isEdited && isContentValid)
            }
    }

    data object Loading : ProfileModStateLegacy
    data object LoadFailed : ProfileModStateLegacy
}

sealed interface ProfileModSideEffectLegacy {
    data object NavigateBack : ProfileModSideEffectLegacy
    data class UpdateProfileImageLegacy(val imageUri: String?) : ProfileModSideEffectLegacy
    data object NavigateToProfileLegacy : ProfileModSideEffectLegacy
}