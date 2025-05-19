package com.acon.acon.feature.spot.screen.spotlist.composable

import android.annotation.SuppressLint
import android.location.Location
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acon.acon.domain.model.spot.v2.Spot
import com.acon.acon.feature.spot.screen.spotlist.SpotListSideEffectV2
import com.acon.acon.feature.spot.screen.spotlist.SpotListViewModel
import com.acon.feature.common.coroutine.collectWithLifecycle
import com.acon.feature.common.location.locationFlow
import com.acon.feature.common.permission.LocationPermissionRequester
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@SuppressLint("MissingPermission")  // Location permission is handled in the LocationPermissionRequester
@Composable
fun SpotListScreenContainer(
    onNavigateToSpotDetailScreen: (Spot) -> Unit,
    onNavigateToExternalMap: (start: Location, destination: Location) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpotListViewModel = hiltViewModel()
) {

    val state by viewModel.collectAsState()
    val userType by viewModel.userType.collectAsStateWithLifecycle()

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

    SpotListScreen(
        state = state,
        userType = userType,
        onSpotTypeChanged = viewModel::onSpotTypeClicked,
        onSpotClick = viewModel::onSpotClicked,
        onTryFindWay = viewModel::onTryFindWay,
        onFilterButtonClick = viewModel::onFilterButtonClicked,
        onFilterModalDismissRequest = viewModel::onFilterModalDismissed,
        onRestaurantFilterSaved = viewModel::onRestaurantFilterSaved,
        onCafeFilterSaved = viewModel::onCafeFilterSaved,
        onGuestItemClick = viewModel::onRequestLogin,
        onGuestModalDismissRequest = viewModel::onDismissLoginModal,
        modifier = modifier.fillMaxSize(),
    )

    LaunchedEffect(isPermissionGranted) {
        if (isPermissionGranted) {
            context.locationFlow()
                .collectWithLifecycle(lifecycleOwner.lifecycle, viewModel::onNewLocationEmitted)
        }
    }

    viewModel.collectSideEffect {
        when (it) {
            is SpotListSideEffectV2.ShowToastMessage -> {
                // Handle the side effect here
            }
            is SpotListSideEffectV2.NavigateToSpotDetailScreen -> {
                onNavigateToSpotDetailScreen(it.spot)
            }
            is SpotListSideEffectV2.NavigateToExternalMap -> {
                onNavigateToExternalMap(it.start, it.destination)
            }
        }
    }
}