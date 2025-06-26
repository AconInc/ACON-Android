package com.acon.acon

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acon.acon.domain.repository.UserRepository
import com.acon.core.type.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AconAppState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getUserType().collectLatest {
                _state.value = state.value.copy(userType = it)
            }
        }
    }

    fun updateShowSignInBottomSheet(show: Boolean) {
        _state.value = state.value.copy(showSignInBottomSheet = show)
    }

    fun updateShowPermissionDialog(show: Boolean) {
        _state.value = state.value.copy(showPermissionDialog = show)
    }

    fun updateAmplPropertyKey(key: String) {
        _state.value = state.value.copy(propertyKey = key)
    }

    fun canOptionalUpdate() {
        _state.value = state.value.copy(showOptionalUpdateModal = true)
    }

    fun shouldUpdate() {
        _state.value = state.value.copy(showForceUpdateModal = true)
    }

    fun dismissOptionalUpdateModal() {
        _state.value = state.value.copy(showOptionalUpdateModal = false)
    }
}

@Immutable
data class AconAppState(
    val snackbarHostState: SnackbarHostState = SnackbarHostState(),
    val userType: com.acon.core.type.UserType = com.acon.core.type.UserType.GUEST,
    val showSignInBottomSheet: Boolean = false,
    val showPermissionDialog: Boolean = false,
    val propertyKey: String = "",
    val showForceUpdateModal: Boolean = false,
    val showOptionalUpdateModal: Boolean = false
)