package com.acon.acon

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
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
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.acon.acon.core.common.DeepLinkHandler
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.SignInBottomSheet
import com.acon.acon.core.designsystem.component.dialog.AconPermissionDialog
import com.acon.acon.core.designsystem.component.dialog.v2.AconDefaultDialog
import com.acon.acon.core.designsystem.component.dialog.v2.AconTwoActionDialog
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.rememberHazeState
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.repository.AconAppRepository
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.core.navigation.route.AreaVerificationRoute
import com.acon.acon.core.navigation.route.SpotRoute
import com.acon.acon.navigation.AconNavigation
import com.acon.acon.core.ads_api.AdProvider
import com.acon.acon.core.ads_api.LocalSpotListAdProvider
import com.acon.acon.core.analytics.amplitude.AconAmplitude
import com.acon.acon.core.analytics.constants.EventNames
import com.acon.acon.provider.ads_impl.SpotListAdProvider
import com.acon.acon.core.common.utils.firstNotNull
import com.acon.acon.core.navigation.LocalNavController
import com.acon.acon.core.ui.compose.LocalDeepLinkHandler
import com.acon.acon.core.ui.compose.LocalLocation
import com.acon.acon.core.ui.compose.LocalRequestLocationPermission
import com.acon.acon.core.ui.compose.LocalRequestSignIn
import com.acon.acon.core.ui.compose.LocalSnackbarHostState
import com.acon.acon.core.ui.compose.LocalUserType
import com.acon.acon.core.ui.android.launchPlayStore
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
import io.branch.referral.Branch
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var socialRepository: SocialRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var aconAppRepository: AconAppRepository

    private val viewModel by viewModels<MainViewModel>()

    private val deepLinkHandler = DeepLinkHandler()

    private val spotListAdProvider: AdProvider = SpotListAdProvider()
    private val gpsResolutionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // User activated GPS
        } else {
            Toast.makeText(this, "GPS를 켜주세요.", Toast.LENGTH_SHORT).show()
        }
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

    private val appInstallStateListener by lazy {
        InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                lifecycleScope.launch {
                    val result = viewModel.state.value.snackbarHostState.showSnackbar(
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

    private val _isLocationPermissionGranted = MutableStateFlow(false)
    private val isLocationPermissionGranted = flow {
        emit(
            checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )

        emitAll(_isLocationPermissionGranted.filter { it })
    }.stateIn(
        scope = lifecycleScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    @SuppressLint("MissingPermission")
    private val currentLocationFlow = callbackFlow<Location> {
        isLocationPermissionGranted.collect { granted ->
            if (granted) {
                val fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(this@MainActivity.applicationContext)
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
            }
        }
    }.stateIn(
        scope = lifecycleScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    private var locationPermissionRequestCount = 0
    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            locationPermissionRequestCount = minOf(locationPermissionRequestCount + 1, 2)

            val allPermissionsGranted = permissions.values.all { it }
            if (allPermissionsGranted) {
                _isLocationPermissionGranted.value = true
            } else {
                requestLocationPermission()
            }
        }

    /**
     * branchUniversalObject(buo) : Branch 딥링크로 전달된 컨텐츠 정보
     * linkProperties : 딥링크의 속성(채널, 파라미터 등)
     * error : 초기화 실패 시 에러 정보
     **/
    // 딥링크 재진입 처리
    // 앱이 실행 중일 때 딥링크가 들어온 경우 (onNewIntent) 기존 인텐트 갱신 + 세션 재초기화 후 파라미터 수신
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.putExtra("branch_force_new_session", true)
        setIntent(intent)

        Branch.sessionBuilder(this).withCallback { buo, _, error ->
            error?.let {
                Timber.e("Branch Error on reInit: ${it.message}")
            }

            buo?.contentMetadata?.customMetadata?.let { metadata ->
                Timber.tag("LifeCycle_Main").d("onNewIntent metadata $metadata")
                deepLinkHandler.handleDeepLink(metadata, true)
            }
        }.reInit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        Branch.sessionBuilder(this).withCallback { buo, _, error ->
            error?.let {
                Timber.e("Branch Error: ${it.message}")
            }

            buo?.contentMetadata?.customMetadata?.let { metadata ->
                Timber.tag("LifeCycle_Main").d("onCreate metadata $metadata")
                deepLinkHandler.handleDeepLink(metadata)
            }
        }.withData(intent?.data).init()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false

        checkGPS()


        lifecycleScope.launch {
            val shouldUpdateAppDeferred = lifecycleScope.async {
                val currentAppVersion = try {
                    val packageInfo =
                        application.packageManager.getPackageInfo(application.packageName, 0)
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
                    if (shouldUpdateAppDeferred.await() == true) { // 2. 강제 업데이트 (스토어 이동)
                        viewModel.shouldUpdate()
                    } else if (updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) { // 3. 선택적 업데이트 (인앱)
                        viewModel.canOptionalUpdate()
                    } else if (updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {  // 1. 강제 업데이트 (인앱)
                        // Not used
                    }
                }
            }
        }

        setContent {
            AconTheme {
                val appState by viewModel.state.collectAsStateWithLifecycle()
                val scope = rememberCoroutineScope()
                val currentLocation by currentLocationFlow.collectAsStateWithLifecycle()
                val navController = rememberNavController()
                val hazeState = rememberHazeState()

                CompositionLocalProvider(
                    LocalLocation provides currentLocation,
                    LocalSnackbarHostState provides appState.snackbarHostState,
                    LocalNavController provides navController,
                    LocalHazeState provides hazeState,
                    LocalUserType provides appState.userType,
                    LocalRequestSignIn provides {
                        viewModel.updateShowSignInBottomSheet(true)
                        viewModel.updateAmplPropertyKey(it)
                    },
                    LocalRequestLocationPermission provides ::requestLocationPermission,
                    LocalSpotListAdProvider provides spotListAdProvider,
                    LocalDeepLinkHandler provides deepLinkHandler
                ) {
                    AconNavigation(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AconTheme.color.Gray900),
                    )

                    if (appState.showSignInBottomSheet) {
                        SignInBottomSheet(
                            onDismissRequest = { viewModel.updateShowSignInBottomSheet(false) },
                            onGoogleSignIn = {
                                scope.launch {
                                    socialRepository.googleSignIn()
                                        .onSuccess {
                                            if (it.hasVerifiedArea) {
                                                navController.navigate(SpotRoute.SpotList) {
                                                    popUpTo(navController.graph.id) {
                                                        inclusive = true
                                                    }
                                                }
                                            } else {
                                                navController.navigate(
                                                    AreaVerificationRoute.AreaVerification(
                                                        verifiedAreaId = null,
                                                        route = "onboarding"
                                                    )
                                                ) {
                                                    popUpTo(navController.graph.id) {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                            if (appState.propertyKey.isNotBlank()) {
                                                AconAmplitude.trackEvent(
                                                    eventName = EventNames.GUEST,
                                                    property = appState.propertyKey to true
                                                )
                                                Timber.d("dddd  " + appState.propertyKey)
                                            }
                                            AconAmplitude.setUserId(it.externalUUID)
                                        }.onFailure {

                                        }
                                    viewModel.updateShowSignInBottomSheet(false)
                                }
                            }, modifier = Modifier
                        )
                    }
                }

                if (appState.showPermissionDialog)
                    AconPermissionDialog(
                        onPermissionGranted = {
                            viewModel.updateShowPermissionDialog(false)
                            _isLocationPermissionGranted.value = true
                        }
                    )
                CheckAndRequireUpdate(appState)
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
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    gpsResolutionResultLauncher.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {

                }
            }
        }
    }

    private fun requestLocationPermission() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) return

        if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            requestMultiplePermissionsLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            if (locationPermissionRequestCount >= 2) {
                viewModel.updateShowPermissionDialog(true)
            } else {
                requestMultiplePermissionsLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    @Composable
    private fun CheckAndRequireUpdate(appState: AconAppState) {
        if (appState.showForceUpdateModal) {
            AconDefaultDialog(
                title = stringResource(R.string.update_required_title),
                action = stringResource(R.string.update),
                onAction = { launchPlayStore() },
                onDismissRequest = {}
            )
        }

        if (appState.showOptionalUpdateModal) {
            AconTwoActionDialog(
                title = stringResource(R.string.update_available_title),
                action1 = stringResource(R.string.cancel),
                action2 = stringResource(R.string.update),
                onAction1 = {
                    viewModel.dismissOptionalUpdateModal()
                }, onAction2 = {
                    viewModel.dismissOptionalUpdateModal()
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo.value ?: return@AconTwoActionDialog,
                        appUpdateActivityResultLauncher,
                        AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE)
                    )
                }, onDismissRequest = {
                    viewModel.dismissOptionalUpdateModal()
                }
            ) {
                Text(
                    text = "업데이트 중에도\n 앱을 계속 이용할 수 있습니다",
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.Gray200,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 22.dp)
                )
            }
        }

        DisposableEffect(appUpdateManager) {
            appUpdateManager.registerListener(appInstallStateListener)
            onDispose {
                appUpdateManager.unregisterListener(appInstallStateListener)
            }
        }
    }
}
