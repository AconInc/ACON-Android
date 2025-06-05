package com.acon.feature.common.base

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import com.acon.feature.common.compose.LocalLocation
import com.acon.feature.common.coroutine.firstNotNull
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitDsl
import org.orbitmvi.orbit.syntax.Syntax

abstract class BaseContainerHost<STATE : Any, SIDE_EFFECT : Any>() :
    ContainerHost<STATE, SIDE_EFFECT>, ViewModel() {

    private val liveLocation = MutableStateFlow<Location?>(null)

    @SuppressLint("ComposableNaming")
    @Composable
    fun emitLiveLocation() {
        val newLocation = LocalLocation.current

        LaunchedEffect(newLocation) {
            liveLocation.emit(newLocation)
            newLocation?.let { onNewLocation(it) }
        }
    }

    protected open fun onNewLocation(location: Location) {}

    suspend fun getCurrentLocation() : Location {
        return liveLocation.firstNotNull()
    }

    @OrbitDsl
    protected suspend inline fun<T> Result<T>.reduceResult(
        syntax: Syntax<STATE, SIDE_EFFECT>,
        crossinline onSuccess: (T) -> STATE,
        crossinline onFailure: (Throwable) -> STATE
    ) : Result<T> {
        return with(syntax) {
            this@reduceResult.onSuccess {
                reduce { onSuccess(it) }
            }.onFailure {
                reduce { onFailure(it) }
            }
        }
    }
}