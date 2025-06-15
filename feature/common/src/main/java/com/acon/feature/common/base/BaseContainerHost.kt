package com.acon.feature.common.base

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import com.acon.acon.domain.type.UserType
import com.acon.feature.common.compose.LocalLocation
import com.acon.feature.common.compose.LocalRequestLocationPermission
import com.acon.feature.common.compose.LocalUserType
import com.acon.feature.common.coroutine.firstNotNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitDsl

@SuppressLint("ComposableNaming")
abstract class BaseContainerHost<STATE : Any, SIDE_EFFECT : Any>() :
    ContainerHost<STATE, SIDE_EFFECT>, ViewModel() {

    private val liveLocation = MutableStateFlow<Location?>(null)

    private val _userType = MutableStateFlow(UserType.GUEST)
    protected val userType = _userType.asStateFlow()

    @Composable
    fun requestLocationPermission() {
        val onRequestLocationPermission = LocalRequestLocationPermission.current

        LaunchedEffect(Unit) {
            onRequestLocationPermission()
        }
    }

    @Composable
    fun emitUserType() {
        val userType = LocalUserType.current

        LaunchedEffect(userType) {
            _userType.value = userType
        }
    }

    @Composable
    fun emitLiveLocation() {
        val newLocation = LocalLocation.current

        LaunchedEffect(newLocation) {
            liveLocation.emit(newLocation)
            newLocation?.let { onNewLocation(it) }
        }
    }

    protected open fun onNewLocation(location: Location) {}

    protected suspend fun getCurrentLocation() : Location {
        return liveLocation.firstNotNull()
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