package com.acon.android

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.acon.android.core.designsystem.theme.AconTheme
import com.acon.android.domain.repository.AuthRepository
import com.acon.android.domain.repository.SocialRepository
import com.acon.android.navigation.AconNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var socialRepository: SocialRepository

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.BLACK, darkScrim = Color.BLACK
            )
        )
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        setContent {
            AconTheme {
                AconNavigation(
                    modifier = Modifier.fillMaxSize(),
                    navController = rememberNavController(),
                    socialRepository = socialRepository,
                    authRepository = authRepository,
                )
            }
        }
    }
}