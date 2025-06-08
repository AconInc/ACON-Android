package com.acon.acon.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.acon.acon.core.designsystem.animation.defaultEnterTransition
import com.acon.acon.core.designsystem.animation.defaultExitTransition
import com.acon.acon.core.designsystem.animation.defaultPopEnterTransition
import com.acon.acon.core.designsystem.animation.defaultPopExitTransition
import com.acon.acon.core.designsystem.component.popup.AconToastPopup
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.signin.screen.SignInRoute
import com.acon.acon.navigation.nested.areaVerificationNavigation
import com.acon.acon.navigation.nested.onboardingNavigationNavigation
import com.acon.acon.navigation.nested.profileNavigation
import com.acon.acon.navigation.nested.settingsNavigation
import com.acon.acon.navigation.nested.signInNavigationNavigation
import com.acon.acon.navigation.nested.spotNavigation
import com.acon.acon.navigation.nested.uploadNavigation
import com.acon.feature.common.compose.LocalNavController
import com.acon.feature.common.compose.LocalSnackbarHostState
import com.acon.feature.common.remember.rememberSocialRepository

@Composable
fun AconNavigation(
    modifier: Modifier = Modifier,
) {
    val socialRepository = rememberSocialRepository()
    val navController = LocalNavController.current
    val snackbarHostState = LocalSnackbarHostState.current

    Scaffold(
        containerColor = AconTheme.color.Gray9,
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 36.dp),
                hostState = snackbarHostState
            ) { snackbarData: SnackbarData ->
                AconToastPopup(
                    minHeight = 56.dp,
                    shape = RoundedCornerShape(8.dp),
                    content = {
                        Text(
                            text = snackbarData.visuals.message,
                            color = AconTheme.color.White,
                            style = AconTheme.typography.Body1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
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
            signInNavigationNavigation(navController, socialRepository)

            areaVerificationNavigation(navController)

            onboardingNavigationNavigation(navController)

            spotNavigation(navController)

            uploadNavigation(navController)

            profileNavigation(navController, socialRepository, snackbarHostState)

            settingsNavigation(navController)
        }
    }
}