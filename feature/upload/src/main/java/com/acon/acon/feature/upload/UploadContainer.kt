package com.acon.acon.feature.upload

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acon.acon.core.designsystem.blur.LocalHazeState
import com.acon.acon.core.designsystem.component.button.AconFilledLargeButton
import com.acon.acon.core.designsystem.component.dialog.AconOneButtonDialog
import com.acon.acon.core.designsystem.component.dialog.AconTwoButtonDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.upload.amplitude.uploadAmplitudeReview
import com.acon.acon.feature.upload.amplitude.uploadAmplitudeSearch
import com.acon.acon.feature.upload.component.DotoriIndicator
import com.acon.acon.feature.upload.component.LocationSearchBottomSheet
import com.acon.acon.feature.upload.component.LocationSelectionButton
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.chrisbanes.haze.hazeSource

@Composable
fun UploadContainer(
    modifier: Modifier = Modifier,
    viewModel: UploadViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSuccess: () -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is UploadSideEffect.NavigateToSuccess -> {
                    onNavigateToSuccess()
                }

                is UploadSideEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    LaunchedEffect(state.showInsufficientDotoriSnackbar) {
        if (state.showInsufficientDotoriSnackbar) {
            snackbarHostState.showSnackbar(
                message = "도토리가 부족해요!",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier.padding(bottom = 112.dp)
            ) {
                SnackbarHost(hostState = snackbarHostState)
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = AconTheme.color.Gray9)
                .padding(paddingValues)
        ) {
            when (state.currentStep) {
                UploadStep.UPLOAD_SEARCH -> {
                    LaunchedEffect(state.currentStep) { uploadAmplitudeSearch() }
                    UploadSearchScreen(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        onIntent = viewModel::onIntent,
                        onShowOutOfServiceDialog = viewModel::onOutOfServiceDialogStateChange,
                        onShowLocationSearchBottomSheet = viewModel::onLocationSearchBottomSheetStateChange
                    )
                }

                UploadStep.UPLOAD_REVIEW -> {
                    UploadReviewScreen(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        onIntent = viewModel::onIntent
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadSearchScreen(
    state: UploadState,
    modifier: Modifier = Modifier,
    onIntent: (UploadIntent) -> Unit = {},
    onShowOutOfServiceDialog: (Boolean) -> Unit = {},
    onShowLocationSearchBottomSheet: (Boolean) -> Unit = {},
) {
    var showExitDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    BackHandler {
        showExitDialog = true
    }

    if (state.showOutOfServiceDialog) {
        AconOneButtonDialog(
            title = "위치 인증 실패",
            content = "현재 인증이 불가능한 지역에 있어요.",
            buttonContent = "확인",
            contentImage = com.acon.acon.core.designsystem.R.drawable.ic_error_2_120,
            onDismissRequest = {},
            onClickConfirm = { onShowOutOfServiceDialog(false) },
            imageSize = 104.dp,
            isImageEnabled = true
        )
    }

    if (showExitDialog) {
        AconTwoButtonDialog(
            title = "작성을 그만둘까요?",
            content = "작성 중인 내용이 저장되지 않아요.",
            leftButtonContent = "그만두기",
            rightButtonContent = "계속하기",
            contentImage = com.acon.acon.core.designsystem.R.drawable.ic_review_g_40,
            onDismissRequest = { showExitDialog = false },
            onClickLeft = {
                showExitDialog = false
                onIntent(UploadIntent.NavigateBack)
            },
            onClickRight = { showExitDialog = false },
            isImageEnabled = false
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .hazeSource(LocalHazeState.current)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .hazeSource(LocalHazeState.current)
        ) {
            AconTopBar(
                leadingIcon = {
                    IconButton(
                        onClick = { showExitDialog = true }
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(
                                id = R.drawable.and_ic_dissmiss_28
                            ),
                            contentDescription = "Close",
                        )
                    }
                },
                content = {
                    Text(
                        text = "업로드",
                        style = AconTheme.typography.head5_22_sb,
                        color = AconTheme.color.White
                    )
                },
                trailingIcon = {
                    Box(
                        modifier = Modifier.clickable(
                            enabled = state.selectedLocation != null,
                            onClick = { onIntent(UploadIntent.OnNextStep) }
                        )
                    ) {
                        Text(
                            text = "다음",
                            style = AconTheme.typography.body2_14_reg,
                            color = if (state.selectedLocation != null)
                                AconTheme.color.White
                            else
                                AconTheme.color.Gray7
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.padding(top = 32.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "장소 등록",
                    style = AconTheme.typography.head8_16_sb,
                    color = AconTheme.color.White,
                )

                Spacer(modifier = Modifier.padding(top = 12.dp))
                LocationSelectionButton(
                    selectedLocation = state.selectedLocation,
                    onClick = { onShowLocationSearchBottomSheet(true) }
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 20.dp))

            if (state.showLocationSearchBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { onShowLocationSearchBottomSheet(false) },
                    sheetState = sheetState,
                    containerColor = AconTheme.color.Gla_w_10,
                    dragHandle = null,
                ) {
                    LocationSearchBottomSheet(
                        onDismiss = { onShowLocationSearchBottomSheet(false) },
                        onLocationSelected = { locationItem ->
                            onShowLocationSearchBottomSheet(false)
                            onIntent(UploadIntent.SelectLocation(locationItem))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UploadReviewScreen(
    modifier: Modifier = Modifier,
    state: UploadState,
    onIntent: (UploadIntent) -> Unit,
) {
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AconTwoButtonDialog(
            title = "작성을 그만둘까요?",
            content = "작성 중인 내용이 저장되지 않아요.",
            leftButtonContent = "그만두기",
            rightButtonContent = "계속하기",
            contentImage = com.acon.acon.core.designsystem.R.drawable.ic_review_g_40,
            onDismissRequest = { showExitDialog = false },
            onClickLeft = {
                showExitDialog = false
                onIntent(UploadIntent.NavigateBack)
            },
            onClickRight = { showExitDialog = false },
            isImageEnabled = false
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(color = AconTheme.color.Gray9)
    ) {
        AconTopBar(
            leadingIcon = {
                IconButton(
                    onClick = { showExitDialog = true }
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(
                            id = com.acon.acon.core.designsystem.R.drawable.ic_dissmiss_24
                        ),
                        contentDescription = "Close",
                    )
                }
            },
            content = {
                Text(
                    text = "업로드",
                    style = AconTheme.typography.title2_20_b,
                    color = AconTheme.color.White
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.padding(top = 32.dp))
            Text(
                text = "도토리를 떨어트려\n리뷰를 남겨 볼까요?",
                style = AconTheme.typography.head6_20_sb,
                color = AconTheme.color.White
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "아래의 도토리 아이콘을 클릭해 리뷰를 남겨주세요.",
                style = AconTheme.typography.body3_13_reg,
                color = AconTheme.color.Gray3
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "보유한 도토리 수 ${state.ownedDotoriCount}/25",
                style = AconTheme.typography.body4_12_reg,
                color = AconTheme.color.Main_org1
            )
        }

        Spacer(modifier = Modifier.padding(top = 52.dp))
        Text(
            text = "${state.selectedCount}/${state.maxCount}",
            style = AconTheme.typography.body4_12_reg,
            color = AconTheme.color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(top = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(state.maxCount) { index ->
                DotoriIndicator(
                    index = index,
                    isSelected = index < state.selectedCount,
                    onClick = {
                        if (index < state.selectedCount) {
                            onIntent(UploadIntent.DeselectDotori(index))
                        } else {
                            onIntent(UploadIntent.SelectDotori(index))
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 12.dp))
        Text(
            text = "도토리를 터치해보세요.",
            style = AconTheme.typography.body2_14_reg,
            color = AconTheme.color.Gray3,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 12.dp, bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.animatingIndex != null) {
                LottieAnimation(
                    composition = rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            when (state.animatingIndex) {
                                0 -> R.raw.dotori1
                                1 -> R.raw.dotori2
                                2 -> R.raw.dotori3
                                3 -> R.raw.dotori4
                                else -> R.raw.dotori5
                            }
                        )
                    ).value,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .scale(1f),
                    iterations = 1,
                    isPlaying = true
                )
            }
        }

        AconFilledLargeButton(
            text = "도토리로 리뷰남기기",
            textStyle = AconTheme.typography.head8_16_sb,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 40.dp),
            enabledBackgroundColor = AconTheme.color.Main_org1,
            disabledBackgroundColor = AconTheme.color.Gray7,
            isEnabled = state.isButtonEnabled,
            onClick = {
                onIntent(UploadIntent.UploadDotori)
                state.selectedLocation?.let {
                    uploadAmplitudeReview(
                        aconNumber = state.selectedCount,
                        spotId = it.spotId
                    )
                }
            }
        )
    }
}
