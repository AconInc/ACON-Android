package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.domain.model.spot.SpotNavigationParameter
import com.acon.acon.domain.model.spot.v2.Spot
import com.acon.acon.domain.type.TransportMode
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.spot.screen.spotlist.SpotListSideEffectV2
import com.acon.acon.feature.spot.screen.spotlist.SpotListViewModel
import com.acon.feature.common.compose.LocalDeepLinkHandler
import com.acon.feature.common.compose.LocalOnRetry
import com.acon.feature.common.compose.LocalRequestSignIn
import com.acon.feature.common.compose.LocalUserType
import kotlinx.coroutines.flow.filter
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SpotListScreenContainer(
    onNavigateToUploadScreen: () -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToSpotDetailScreen: (Spot, TransportMode) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToDeeplinkSpotDetailScreen:(spotNav: SpotNavigationParameter) -> Unit = {},
    viewModel: SpotListViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val deepLinkHandler = LocalDeepLinkHandler.current
    val context = LocalContext.current

    val userType = LocalUserType.current
    val onSignInRequired = LocalRequestSignIn.current

    LaunchedEffect(Unit) {
        deepLinkHandler.spotIdFlow
            .filter { it != -1L }
            .collect { spotId ->
                onNavigateToDeeplinkSpotDetailScreen(
                    SpotNavigationParameter(
                        spotId = spotId,
                        tags = emptyList(),
                        transportMode = null,
                        eta = null,
                        isFromDeepLink = true,
                        navFromProfile = null
                    )
                )
                deepLinkHandler.clear()
            }
    }

    CompositionLocalProvider(LocalOnRetry provides viewModel::retry) {
        SpotListScreen(
            state = state,
            onSpotTypeChanged = viewModel::onSpotTypeClicked,
            onSpotClick = { spot, rank ->
                if (userType == UserType.GUEST)
                    onSignInRequired("click_detail_guest?")
                else
                    viewModel.onSpotClicked(spot, rank)
            },
            onTryFindWay = viewModel::onTryFindWay,
            onNavigationAppChoose = viewModel::onNavigationAppChosen,
            onChooseNavigationAppModalDismiss = viewModel::onChooseNavigationAppModalDismissed,
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
                it.handler.startNavigationApp(context)
            }
        }
    }
}