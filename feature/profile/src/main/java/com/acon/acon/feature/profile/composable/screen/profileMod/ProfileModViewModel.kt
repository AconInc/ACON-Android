package com.acon.acon.feature.profile.composable.screen.profileMod

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acon.acon.core.designsystem.component.textfield.TextFieldStatus
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.repository.UploadRepository
import com.acon.acon.feature.profile.composable.screen.profile.ProfileUiState
import dagger.hilt.android.internal.Contexts.getApplication
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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileModViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    application: Application
) : AndroidViewModel(application), ContainerHost<ProfileModState, ProfileModSideEffect> {

    override val container = container<ProfileModState, ProfileModSideEffect>(ProfileModState())

    fun onFocusChanged(isFocused: Boolean) = intent {
        reduce {
            state.copy(
                nickNameFieldStatus = if (isFocused) TextFieldStatus.Focused else TextFieldStatus.Inactive,
                birthdayFieldStatus = if (isFocused) TextFieldStatus.Focused else TextFieldStatus.Inactive
            )
        }
    }

    private fun Char.isAllowedChar(): Boolean {
        return this in 'a'..'z' ||
                this in 'A'..'Z' ||
                this in '0'..'9' ||
                this in '가'..'힣' ||
                this in 'ㄱ'..'ㅎ' ||
                this in 'ㅏ'..'ㅣ' ||
                this == '.' || this == '_'
    }


    private var nicknameValidationJob: Job? = null

    fun onNicknameChanged(text: String) = intent {
        val filteredText = text.filter { it.isAllowedChar() }
        reduce {
            state.copy(nickNameState = filteredText, nicknameStatus = NicknameStatus.Typing)
        }

        nicknameValidationJob?.cancel()

        nicknameValidationJob = viewModelScope.launch {
            delay(500L)

            val errors = mutableListOf<NicknameErrorType>()

            if (text.any { it in "!@#$%^&*()-=+[]{};:'\",<>/?\\|" }) {
                errors.add(NicknameErrorType.InvalidChar)
            }

            val allowedChars = (33..126).map { it.toChar() } +
                    ('가'..'힣') + ('ㄱ'..'ㅎ') + ('ㅏ'..'ㅣ')
            if (text.any { it !in allowedChars }) {
                errors.add(NicknameErrorType.InvalidLang)
            }

            val alreadyUsed = false //TODO: 서버 체크 로직 추가
            if(alreadyUsed) {
                errors.add(NicknameErrorType.AlreadyUsed)
            }

            reduce {
                state.copy(
                    nicknameStatus = when {
                        filteredText.isEmpty() -> NicknameStatus.Empty
                        errors.isNotEmpty() -> NicknameStatus.Error(errors)
                        else -> NicknameStatus.Valid
                    }
                )
            }
        }
    }

    fun onBirthdayChanged(text: String) = intent {
        val formattedText = formatBirthdayInput(text)
        reduce {
            state.copy(birthdayState = formattedText)
        }

        if (formattedText.length == 10) {
            if (validateBirthday(formattedText)) {
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

    private fun formatBirthdayInput(input: String): String {
        val digitsOnly = input.filter { it.isDigit() }

        return when {
            digitsOnly.length <= 4 -> digitsOnly
            digitsOnly.length <= 6 -> "${digitsOnly.substring(0, 4)}.${digitsOnly.substring(4)}"
            digitsOnly.length <= 8 -> "${digitsOnly.substring(0, 4)}.${digitsOnly.substring(4, 6)}.${digitsOnly.substring(6)}"
            else -> "${digitsOnly.substring(0, 4)}.${digitsOnly.substring(4, 6)}.${digitsOnly.substring(6, 8)}"
        }
    }

    private fun validateBirthday(birthday: String): Boolean {
        val parts = birthday.split(".")
        if (parts.size != 3) return false

        val year = parts[0].toIntOrNull() ?: return false
        val month = parts[1].toIntOrNull() ?: return false
        val day = parts[2].toIntOrNull() ?: return false

        val currentYear = java.time.Year.now().value
        if (year > currentYear || year <= 1900) return false

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

    fun showDialog() = intent {
        reduce {
            state.copy(showDialog = true)
        }
    }

    fun hideDialog() = intent {
        reduce {
            state.copy(showDialog = false)
        }
    }

    fun fetchVerifiedAreaList(){
        //TODO: 서버 통해서 사용자가 인증 받은 동네 리스트 가져오기
        //가져온 리스트로 verifiedAreaList 관리하기
    }

    fun removeVerifiedArea(area: String) = intent {
        val updatedList = state.verifiedAreaList.filterNot { it == area }

        reduce {
            state.copy(
                verifiedAreaList = updatedList,
                showAreaDeleteDialog = false
            )
        }
    }

    fun showAreaDeleteDialog(area: String) = intent {
        reduce {
            state.copy(
                showAreaDeleteDialog = true,
                selectedArea = area
            )
        }
    }

    fun hideAreaDeleteDialog() = intent {
        reduce {
            state.copy(showAreaDeleteDialog = false)
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
        viewModelScope.launch {
            profileRepository.getPreSignedUrl()
                .onSuccess { result ->   //성공시 얻은 PreSignedUrl값 저장하고, fileName 저장하고, PUT 함수 부르기
                    reduce {
                        state.copy(
                            uploadFileName = result.fileName,
                            preSignedUrl = result.preSignedUrl
                        )
                    }
                    putPhotoToPreSignedUrl(Uri.parse(state.selectedPhotoUri), state.preSignedUrl)
                }
                .onFailure {
                    // 실패시 에러처리 어케하지?
                }
        }
    }

    private fun putPhotoToPreSignedUrl(imageUri: Uri, preSignedUrl: String) = intent {
        val context = getApplication<Application>().applicationContext
        val client = OkHttpClient()

        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val byteArray = inputStream?.readBytes() ?: throw IllegalArgumentException("Failed to read image")

            val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"

            val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), byteArray)

            val request = Request.Builder()
                .url(preSignedUrl)
                .put(fileBody)
                .addHeader("Content-Type", mimeType)
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                // PUT 성공 시 정상적으로 프로필 수정 API 호출 (fileName 사용)
            } else {
                // PUT 실패 시 에러 처리
            }
        } catch (e: Exception) {
            // ImageUri 갖고 바이너리 방식으로 변환 과정에서 에러 처리
        }
    }



}


data class ProfileModState(

    val nickNameFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
    val nickNameState: String = "",
    val nicknameStatus: NicknameStatus = NicknameStatus.Empty,

    val birthdayFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
    val birthdayState: String = "",
    val birthdayStatus: BirthdayStatus = BirthdayStatus.Empty,

    val verifiedAreaList: List<String> = listOf("쌍문동"),

    val showDialog: Boolean = false,
    val showAreaDeleteDialog: Boolean = false,
    val selectedArea: String? = null,

    val requestPhotoPermission: Boolean = false,
    val showPermissionDialog: Boolean = false,

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
    data object Valid :BirthdayStatus()
}

sealed interface ProfileModSideEffect {
    data object NavigateBack : ProfileModSideEffect
    data class NavigateToSettings(val packageName: String) : ProfileModSideEffect
    data object NavigateToCustomGallery : ProfileModSideEffect
    data class UpdateProfileImage(val imageUri: String?) : ProfileModSideEffect
}