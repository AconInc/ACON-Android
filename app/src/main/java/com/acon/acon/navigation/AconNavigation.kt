package com.acon.acon.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.acon.acon.core.designsystem.animation.defaultEnterTransition
import com.acon.acon.core.designsystem.animation.defaultExitTransition
import com.acon.acon.core.designsystem.animation.defaultPopEnterTransition
import com.acon.acon.core.designsystem.animation.defaultPopExitTransition
import com.acon.acon.core.designsystem.blur.LocalHazeState
import com.acon.acon.core.designsystem.blur.defaultHazeEffect
import com.acon.acon.core.designsystem.blur.rememberHazeState
import com.acon.acon.core.designsystem.component.bottomsheet.LoginBottomSheet
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.constants.AppURL
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.feature.areaverification.AreaVerificationRoute
import com.acon.acon.feature.profile.composable.ProfileRoute
import com.acon.acon.feature.signin.screen.SignInRoute
import com.acon.acon.feature.spot.SpotRoute
import com.acon.acon.feature.upload.UploadRoute
import com.acon.acon.navigation.bottom.BottomBar
import com.acon.acon.navigation.bottom.BottomNavType
import com.acon.acon.navigation.nested.areaVerificationNavigation
import com.acon.acon.navigation.nested.onboardingNavigationNavigation
import com.acon.acon.navigation.nested.profileNavigation
import com.acon.acon.navigation.nested.settingsNavigation
import com.acon.acon.navigation.nested.signInNavigationNavigation
import com.acon.acon.navigation.nested.splashNavigationNavigation
import com.acon.acon.navigation.nested.spotNavigation
import com.acon.acon.navigation.nested.uploadNavigation
import kotlinx.coroutines.launch

@Composable
fun AconNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    socialRepository: SocialRepository,
    userRepository: UserRepository,
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    var selectedBottomNavItem by rememberSaveable { mutableStateOf(BottomNavType.SPOT) }
    val currentRoute by remember { derivedStateOf { backStackEntry?.destination?.route } }

    val hazeState = rememberHazeState()

    val context = LocalContext.current
    val isLogin = userRepository.getLoginState().collectAsState()
    var showLoginBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    CompositionLocalProvider(LocalHazeState provides hazeState) {
        if(showLoginBottomSheet) {
            LoginBottomSheet(
                hazeState = LocalHazeState.current,
                onDismissRequest = { showLoginBottomSheet = false },
                onGoogleSignIn = {
                    coroutineScope.launch {
                        socialRepository.signIn()
                            .onSuccess {
                                if (it.hasVerifiedArea) {
                                    showLoginBottomSheet = false
                                    navController.navigate(SpotRoute.SpotList) {
                                        popUpTo<AreaVerificationRoute.Graph> {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    showLoginBottomSheet = false
                                    navController.navigate(AreaVerificationRoute.RequireAreaVerification) {
                                        popUpTo<AreaVerificationRoute.Graph> {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                            .onFailure {
                                showLoginBottomSheet = false
                            }
                    }
                },
                onTermOfUse = {
                    val url = AppURL.TERM_OF_USE
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                },
                onPrivatePolicy = {
                    val url = AppURL.PRIVATE_POLICY
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            )
        }

        Scaffold(
            containerColor = AconTheme.color.Gray9,
            modifier = modifier.navigationBarsPadding(),
            topBar = {
                Spacer(modifier = Modifier.padding(0.dp))
            },
            bottomBar = {
                if (backStackEntry?.destination?.shouldShowBottomNav() == true) {
                    BottomBar(
                        modifier = Modifier
                            .background(color = AconTheme.color.Black)  // TODO Color?
                            .fillMaxWidth()
                            .defaultHazeEffect(
                                hazeState = LocalHazeState.current,
                                tintColor = AconTheme.color.Dim_b_30
                            ),
                        selectedItem = selectedBottomNavItem,
                        onItemClick = { item ->
                            if (item == BottomNavType.UPLOAD) {
                                if(isLogin.value) {
                                    navController.navigate(UploadRoute.Upload)
                                } else {
                                    showLoginBottomSheet = true

                                }
                            } else {
                                selectedBottomNavItem = item
                                navController.navigate(
                                    when (item) {
                                        BottomNavType.SPOT -> SpotRoute.SpotList
                                        BottomNavType.PROFILE -> ProfileRoute.Profile
                                        else -> SpotRoute.SpotList
                                    }
                                ) {
                                    popUpTo(SpotRoute.SpotList) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }
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
                splashNavigationNavigation(navController)

                signInNavigationNavigation(navController, socialRepository)

                areaVerificationNavigation(navController)

                onboardingNavigationNavigation(navController)

                spotNavigation(navController, socialRepository)

                uploadNavigation(navController)

                profileNavigation(navController, socialRepository)

                settingsNavigation(navController)
            }
        }
    }

    LaunchedEffect(currentRoute) {   // 뒤로가기에 의한 하단 탭 선택 상태 변경 처리
        selectedBottomNavItem = when (currentRoute) {
            SpotRoute.SpotList::class.qualifiedName -> BottomNavType.SPOT
            ProfileRoute.Profile::class.qualifiedName -> BottomNavType.PROFILE
            else -> BottomNavType.SPOT // TODO : Route
        }
    }
}

private fun NavDestination.shouldShowBottomNav(): Boolean {
    return when (route) {
        SpotRoute.SpotList::class.qualifiedName -> true
        ProfileRoute.Profile::class.qualifiedName -> true
        else -> false
    }
}