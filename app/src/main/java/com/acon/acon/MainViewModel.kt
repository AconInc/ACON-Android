package com.acon.acon

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.UserType
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
}

@Immutable
data class AconAppState(
    val userType: UserType = UserType.GUEST,
    val showSignInBottomSheet: Boolean = false,
    val showPermissionDialog: Boolean = false,
)