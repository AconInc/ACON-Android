package com.acon.acon.feature.spot.screen.spotlist.composable

import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.domain.model.spot.v2.Spot
import com.acon.acon.domain.type.TransportMode
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.spot.screen.spotlist.SpotListSideEffectV2
import com.acon.acon.feature.spot.screen.spotlist.SpotListViewModel
import com.acon.feature.common.compose.LocalOnRetry
import com.acon.feature.common.compose.LocalRequestSignIn
import com.acon.feature.common.compose.LocalUserType
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SpotListScreenContainer(
    onNavigateToUploadScreen: () -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToSpotDetailScreen: (Spot, TransportMode) -> Unit,
    onNavigateToExternalMap: (start: Location, destination: Location, destinationName: String, transportMode: TransportMode) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpotListViewModel = hiltViewModel()
) {

    val state by viewModel.collectAsState()

    val userType = LocalUserType.current
    val onSignInRequired = LocalRequestSignIn.current

    CompositionLocalProvider(LocalOnRetry provides viewModel::retry) {
        SpotListScreen(
            state = state,
            onSpotTypeChanged = viewModel::onSpotTypeClicked,
            onSpotClick = { spot, rank ->
                if (userType == UserType.GUEST)
                    onSignInRequired()
                else
                    viewModel.onSpotClicked(spot, rank)
            },
            onTryFindWay = viewModel::onTryFindWay,
            onFilterButtonClick = viewModel::onFilterButtonClicked,
            onFilterModalDismissRequest = viewModel::onFilterModalDismissed,
            onRestaurantFilterSaved = viewModel::onRestaurantFilterSaved,
            onCafeFilterSaved = viewModel::onCafeFilterSaved,
            modifier = modifier.fillMaxSize(),
            onNavigateToUploadScreen = onNavigateToUploadScreen,
            onNavigateToProfileScreen = onNavigateToProfileScreen
        )
    }

    viewModel.requestLocationPermission()
    viewModel.emitUserType()
    viewModel.emitLiveLocation()
    viewModel.collectSideEffect {
        when (it) {
            is SpotListSideEffectV2.ShowToastMessage -> {
                // Handle the side effect here
            }
            is SpotListSideEffectV2.NavigateToSpotDetailScreen -> {
                onNavigateToSpotDetailScreen(it.spot, it.transportMode)
            }
            is SpotListSideEffectV2.NavigateToExternalMap -> {
                onNavigateToExternalMap(it.start, it.destination, it.destinationName, it.transportMode)
            }
        }
    }
}