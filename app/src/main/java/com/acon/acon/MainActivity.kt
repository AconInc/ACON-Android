package com.acon.acon

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.navigation.AconNavigation
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var socialRepository: SocialRepository

    @Inject
    lateinit var userRepository: UserRepository

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

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
                    userRepository = userRepository
                )
            }
        }
    }

    private suspend fun getAppUpdateType(): Int {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        return suspendCoroutine { continuation ->
            appUpdateInfoTask.addOnSuccessListener { updateInfo ->
                if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    if (updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        continuation.resume(FORCE)
                    } else if (updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        continuation.resume(OPTIONAL)
                    } else {
                        continuation.resume(NONE)
                    }
                }
            }.addOnFailureListener {
                continuation.resume(NONE)
            }
        }
    }

    private fun checkAppUpdate() {
        lifecycleScope.launch {
            if (viewModel.fetchShouldUpdateApp() || getAppUpdateType() == FORCE) {

            } else if (getAppUpdateType() == OPTIONAL) {
                // 선택적 업데이트
            } else {
                // 업데이트 필요 없음
            }
        }
    }

    companion object AppUpdate {
        private const val FORCE = 2
        private const val OPTIONAL = 1
        private const val NONE = 0
    }
}