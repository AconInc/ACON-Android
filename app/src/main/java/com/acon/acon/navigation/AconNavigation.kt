package com.acon.acon.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import com.acon.acon.core.designsystem.animation.defaultEnterTransition
import com.acon.acon.core.designsystem.animation.defaultExitTransition
import com.acon.acon.core.designsystem.animation.defaultPopEnterTransition
import com.acon.acon.core.designsystem.animation.defaultPopExitTransition
import com.acon.acon.core.designsystem.component.popup.AconToastPopup
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.navigation.nested.areaVerificationNavigation
import com.acon.acon.navigation.nested.onboardingNavigationNavigation
import com.acon.acon.navigation.nested.profileNavigation
import com.acon.acon.navigation.nested.settingsNavigation
import com.acon.acon.navigation.nested.signInNavigationNavigation
import com.acon.acon.navigation.nested.spotNavigation
import com.acon.acon.navigation.nested.uploadNavigation
import com.acon.core.model.spot.SpotNavigationParameter
import com.acon.acon.core.navigation.LocalNavController
import com.acon.acon.core.navigation.route.SignInRoute
import com.acon.acon.core.navigation.route.SpotRoute
import com.acon.acon.core.ui.compose.LocalDeepLinkHandler
import com.acon.acon.core.ui.compose.LocalSnackbarHostState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@Composable
fun AconNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current
    val snackbarHostState = LocalSnackbarHostState.current

    val deepLinkHandler = LocalDeepLinkHandler.current

    val isWarmStart by deepLinkHandler.isWarmStart.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, isWarmStart) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && isWarmStart) {
                lifecycleOwner.lifecycleScope.launch {
                    deepLinkHandler.spotIdFlow
                        .filter { it > 0 }
                        .take(1)
                        .collect { spotId ->
                            delay(400)
                            navController.navigate(
                                SpotRoute.SpotDetail(
                                    SpotNavigationParameter(
                                        spotId = spotId,
                                        tags = emptyList(),
                                        transportMode = null,
                                        eta = null,
                                        isFromDeepLink = true,
                                        navFromProfile = null
                                    )
                                )
                            ) {
                                launchSingleTop = false
                            }
                            deepLinkHandler.clear()
                        }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        containerColor = AconTheme.color.Gray9,
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 120.dp),
                hostState = snackbarHostState
            ) { snackbarData: SnackbarData ->
                AconToastPopup(
                    shape = RoundedCornerShape(8.dp),
                    horizontalArrangement = if (snackbarData.visuals.actionLabel != null) Arrangement.Start else Arrangement.Center,
                    contentPadding = PaddingValues(vertical = 13.dp, horizontal = 12.dp),
                    content = {
                        Text(
                            text = snackbarData.visuals.message,
                            color = AconTheme.color.White,
                            style = AconTheme.typography.Body1,
                            textAlign = TextAlign.Center,
                        )
                        if (snackbarData.visuals.actionLabel != null) {
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = snackbarData.visuals.actionLabel!!,
                                color = AconTheme.color.Action,
                                style = AconTheme.typography.Body1,
                                modifier = Modifier.noRippleClickable {
                                    snackbarData.performAction()
                                }
                            )
                        }
                    }
                )
            }
        },
        topBar = { Spacer(modifier = Modifier.padding(0.dp)) },
        bottomBar = { Spacer(modifier = Modifier.padding(0.dp)) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SignInRoute.Graph,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                defaultEnterTransition()
            }, exitTransition = {
                defaultExitTransition()
            }, popEnterTransition = {
                defaultPopEnterTransition()
            }, popExitTransition = {
                defaultPopExitTransition()
            }
        ) {
            signInNavigationNavigation(navController)

            areaVerificationNavigation(navController)

            onboardingNavigationNavigation(navController)

            spotNavigation(navController)

            uploadNavigation(navController)

            profileNavigation(navController, snackbarHostState)

            settingsNavigation(navController)
        }
    }
}