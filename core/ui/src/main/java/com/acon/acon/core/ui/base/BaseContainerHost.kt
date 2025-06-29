package com.acon.acon.core.ui.base

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import com.acon.acon.core.common.utils.firstNotNull
import com.acon.acon.core.model.type.UserType
import com.acon.acon.core.ui.compose.LocalLocation
import com.acon.acon.core.ui.compose.LocalRequestLocationPermission
import com.acon.acon.core.ui.compose.LocalUserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.suspendCancellableCoroutine
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitDsl

@SuppressLint("ComposableNaming")
abstract class BaseContainerHost<STATE : Any, SIDE_EFFECT : Any>() :
    ContainerHost<STATE, SIDE_EFFECT>, ViewModel() {

    private val currentLocation = MutableStateFlow<Location?>(null)

    private val _userType = MutableStateFlow(com.acon.acon.core.model.type.UserType.GUEST)
    protected val userType = _userType.asStateFlow()

    @Composable
    fun requestLocationPermission() {
        val onRequestLocationPermission = LocalRequestLocationPermission.current

        LaunchedEffect(Unit) {
            onRequestLocationPermission()
        }
    }

    @Composable
    fun useUserType() {
        val userType = LocalUserType.current

        LaunchedEffect(Unit) {
            snapshotFlow { userType }.collect {
                _userType.value = userType
            }
        }
    }

    @Composable
    fun useLiveLocation() {
        val newLocation = LocalLocation.current

        LaunchedEffect(Unit) {
            snapshotFlow { newLocation }.filterNotNull().collect {
                currentLocation.emit(it)
                onNewLocation(it)
            }
        }
    }

    protected open fun onNewLocation(location: Location) {}

    protected suspend fun getCurrentLocation() : Location {
        return currentLocation.firstNotNull()
    }

    @OrbitDsl
    protected suspend inline fun<T> Result<T>.reduceResult(
        crossinline onSuccess: (T) -> STATE,
        crossinline onFailure: (Throwable) -> STATE
    ) : Result<T> {
        return suspendCancellableCoroutine { continuation ->
            intent {
                continuation.resume(this@reduceResult.onSuccess {
                    reduce { onSuccess(it) }
                }.onFailure {
                    reduce { onFailure(it) }
                }) { _, _, _ -> }
            }
        }
    }
}