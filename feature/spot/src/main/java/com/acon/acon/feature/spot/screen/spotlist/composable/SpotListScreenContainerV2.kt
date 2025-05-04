package com.acon.acon.feature.spot.screen.spotlist.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.acon.acon.feature.spot.screen.spotlist.SpotListViewModelV2
import com.acon.feature.common.coroutine.collectWithLifecycle
import com.acon.feature.common.location.locationFlow
import com.acon.feature.common.permission.LocationPermissionRequester
import org.orbitmvi.orbit.compose.collectAsState

@SuppressLint("MissingPermission")  // Location permission is handled in the LocationPermissionRequester
@Composable
fun SpotListScreenContainerV2(
    modifier: Modifier = Modifier,
    viewModel: SpotListViewModelV2 = hiltViewModel()
) {

    val state by viewModel.collectAsState()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isPermissionGranted by remember {
        mutableStateOf(false)
    }

    LocationPermissionRequester(
        onPermissionGranted = {
            isPermissionGranted = true
        }
    )

    SpotListScreenV2(
        state = state,
        onSpotTypeChanged = viewModel::onSpotTypeChanged,
        modifier = modifier.fillMaxSize(),
    )

    LaunchedEffect(isPermissionGranted) {
        if (isPermissionGranted) {
            context.locationFlow()
                .collectWithLifecycle(lifecycleOwner.lifecycle, viewModel::onNewLocationEmitted)
        }
    }
}