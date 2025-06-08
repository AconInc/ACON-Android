package com.acon.acon

import android.annotation.SuppressLint
import android.content.IntentSender
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.SignInBottomSheet
import com.acon.acon.core.designsystem.component.dialog.v2.AconDefaultDialog
import com.acon.acon.core.designsystem.component.dialog.v2.AconTwoActionDialog
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.rememberHazeState
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.repository.AconAppRepository
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.areaverification.AreaVerificationRoute
import com.acon.acon.feature.spot.SpotRoute
import com.acon.acon.navigation.AconNavigation
import com.acon.feature.common.compose.LocalLocation
import com.acon.feature.common.compose.LocalNavController
import com.acon.feature.common.compose.LocalRequestSignIn
import com.acon.feature.common.compose.LocalSnackbarHostState
import com.acon.feature.common.compose.LocalUserType
import com.acon.feature.common.coroutine.firstNotNull
import com.acon.feature.common.intent.launchPlayStore
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var socialRepository: SocialRepository
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var aconAppRepository: AconAppRepository

    private val gpsResolutionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // User activated GPS
        } else {
            Toast.makeText(this, "GPS를 켜주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private val userType by lazy {
        userRepository.getUserType().stateIn(
            scope = lifecycleScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserType.GUEST
        )
    }

    private val appUpdateManager by lazy {
        AppUpdateManagerFactory.create(application)
    }
    private val appUpdateInfo = flow {
        emit(appUpdateManager.appUpdateInfo.await())
    }.stateIn(
        scope = lifecycleScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    private val updateStateFlow = flow {
        val shouldUpdateAppDeferred = lifecycleScope.async {
            val currentAppVersion = try {
                val packageInfo = application.packageManager.getPackageInfo(application.packageName, 0)
                packageInfo.versionName
            } catch (e: Exception) {
                null
            }
            currentAppVersion?.let { v ->
                aconAppRepository.shouldUpdateApp(v).getOrElse { false }
            }
        }

        appUpdateInfo.firstNotNull().let { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {  // 1. 강제 업데이트 (인앱)
                    // Not used
                } else if (shouldUpdateAppDeferred.await() == true) { // 2. 강제 업데이트 (스토어 이동)
                    emit(UpdateState.FORCE)
                } else if (updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) { // 3. 선택적 업데이트 (인앱)
                    emit(UpdateState.OPTIONAL)
                }
            }
        }
    }.stateIn(
        scope = lifecycleScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UpdateState.NONE
    )

    private val appInstallStateListener by lazy {
        InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                lifecycleScope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = getString(R.string.update_complete),
                        actionLabel = getString(R.string.restart)
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            appUpdateManager.completeUpdate()
                        }

                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }
        }
    }
    private val snackbarHostState = SnackbarHostState()
    private val appUpdateActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {   // Immediate에서는 받을 일 없음
                Timber.d("유저 업데이트 수락")
            } else if (result.resultCode == RESULT_CANCELED) {
                Timber.d("유저 업데이트 거부")
            } else if (result.resultCode == ActivityResult.RESULT_IN_APP_UPDATE_FAILED) {
                Timber.d("업데이트 실패")
            }
        }

    @SuppressLint("MissingPermission")
    private val currentLocationFlow = callbackFlow<Location> {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity.applicationContext)
        trySend(
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).await()
        )

        val locationRequest = LocationRequest.Builder(3_000).setPriority(
            Priority.PRIORITY_HIGH_ACCURACY
        ).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Timber.d("새 좌표 획득: [${location.latitude}, ${location.longitude}]")
                    trySend(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }.stateIn(
        scope = lifecycleScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }
        super.onCreate(savedInstanceState)

        // TODO - 현재 기기를 테스트 디바이스로 등록 -> 테스트 광고 노출
        val testDevices = listOf("559854319F9393CE36A962FE0E09E02B", "2470BB3FFC6EB2D0A79866A27F78FBCD")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDevices).build()
        MobileAds.setRequestConfiguration(configuration)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        enableEdgeToEdge()
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false

        checkGPS()

        setContent {
            AconTheme {
                val scope = rememberCoroutineScope()
                val currentLocation by currentLocationFlow.collectAsStateWithLifecycle()
                val navController = rememberNavController()
                val hazeState = rememberHazeState()
                val userType by this.userType.collectAsStateWithLifecycle()
                var showSignInBottomSheet by remember { mutableStateOf(false) }

                CheckAndRequireUpdate()

                CompositionLocalProvider(
                    LocalLocation provides currentLocation,
                    LocalSnackbarHostState provides snackbarHostState,
                    LocalNavController provides navController,
                    LocalHazeState provides hazeState,
                    LocalUserType provides userType,
                    LocalRequestSignIn provides { showSignInBottomSheet = true }
                ) {
                    AconNavigation(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AconTheme.color.Gray900),
                    )
                }

                if (showSignInBottomSheet) {
                    SignInBottomSheet(
                        onDismissRequest = { showSignInBottomSheet = false },
                        onGoogleSignIn = {
                            scope.launch {
                                socialRepository.googleSignIn()
                                    .onSuccess {
                                        showSignInBottomSheet = false
                                        if (it.hasVerifiedArea) {
                                            navController.navigate(SpotRoute.SpotList) {
                                                popUpTo<AreaVerificationRoute.Graph> {
                                                    inclusive = true
                                                }
                                            }
                                        } else {
                                            navController.navigate(
                                                AreaVerificationRoute.AreaVerification(
                                                    "onboarding"
                                                )
                                            ) {
                                                popUpTo<AreaVerificationRoute.Graph> {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                    .onFailure {
                                        showSignInBottomSheet = false
                                    }
                            }
                        },
                    )
                }
            }
        }
    }

    private fun checkGPS() {
        val locationRequest = LocationRequest.Builder(3_000L).setPriority(
            Priority.PRIORITY_HIGH_ACCURACY
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            // GPS 원래 활성화 상태
        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    gpsResolutionResultLauncher.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {

                }
            }
        }
    }

    @Composable
    private fun CheckAndRequireUpdate() {
        val updateState by updateStateFlow.collectAsStateWithLifecycle()

        when (updateState) {
            UpdateState.FORCE -> {
                AconDefaultDialog(
                    title = stringResource(R.string.update_required_title),
                    action = stringResource(R.string.update),
                    onAction = { launchPlayStore() },
                    onDismissRequest = {}
                )
            }

            UpdateState.OPTIONAL -> {
                AconTwoActionDialog(
                    title = stringResource(R.string.update_available_title),
                    action1 = stringResource(R.string.cancel),
                    action2 = stringResource(R.string.update),
                    onAction1 = {}, onAction2 = {
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo.value ?: return@AconTwoActionDialog,
                            appUpdateActivityResultLauncher,
                            AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE)
                        )
                    }, onDismissRequest = {}
                )
            }

            UpdateState.NONE -> Unit
        }

        DisposableEffect(appUpdateManager) {
            appUpdateManager.registerListener(appInstallStateListener)
            onDispose {
                appUpdateManager.unregisterListener(appInstallStateListener)
            }
        }
    }
}

enum class UpdateState {
    FORCE, OPTIONAL, NONE
}