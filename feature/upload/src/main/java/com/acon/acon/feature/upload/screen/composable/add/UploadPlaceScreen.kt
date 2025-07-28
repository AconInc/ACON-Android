package com.acon.acon.feature.upload.screen.composable.add

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.component.dialog.v2.AconTwoActionDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.ui.compose.getScreenWidth
import com.acon.acon.feature.upload.screen.UploadPlaceViewModel
import com.acon.acon.feature.upload.screen.composable.add.image.UploadPlaceImageScreen
import com.acon.acon.feature.upload.screen.composable.add.place.UploadSelectPlaceDetailScreen
import com.acon.acon.feature.upload.screen.composable.add.place.UploadSelectPlaceScreen
import com.acon.acon.feature.upload.screen.composable.add.price.UploadSelectPriceScreen
import com.acon.acon.feature.upload.screen.composable.add.search.UploadPlaceSearchScreen
import com.acon.acon.feature.upload.screen.composable.menu.UploadPlaceEnterMenuScreen
import dev.chrisbanes.haze.hazeSource
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun UploadPlaceScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UploadPlaceViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    val screenWidthDp = getScreenWidth()
    val dialogWidth = (screenWidthDp * (260f / 360f))

    var currentStep by remember { mutableIntStateOf(0) }
    val steps = listOf<@Composable () -> Unit>(
        { UploadPlaceSearchScreen() },
        {
            UploadSelectPlaceScreen(
                state = state,
                onSelectSpotType = viewModel::updateSpotType,
                onUpdateNextPageBtnEnabled = viewModel::updateNextBtnEnabled
            )
        },
        { UploadSelectPlaceDetailScreen(
            state = state,
            onUpdateCafeOptionType = viewModel::updateCafeOptionType,
            onUpdateRestaurantType = viewModel::updateRestaurantType,
            onUpdateNextPageBtnEnabled = viewModel::updateNextBtnEnabled
        ) },
        { UploadPlaceEnterMenuScreen() },
        { UploadSelectPriceScreen() },
        { UploadPlaceImageScreen() },
        { UploadPlaceCompleteScreen() }
    )

    LaunchedEffect(currentStep) {
        if (currentStep == 0) {
            viewModel.onPreviousBtnDisabled()
            viewModel.updateNextBtnEnabled(true) // TODO - 임시조건 (검색 구현 후 삭제)
        } else {
            viewModel.onPreviousBtnEnabled()
        }
    }

    if(state.showExitUploadPlaceDialog) {
        AconTwoActionDialog(
            title = stringResource(R.string.upload_place_exit),
            action1 = stringResource(R.string.cancel),
            action2 = stringResource(R.string.exit),
            onDismissRequest = {},
            onAction1 = {
                viewModel.dismissExitUploadPlaceDialog()
            },
            onAction2 = {
                onNavigateBack()
            },
            modifier = Modifier.width(dialogWidth)
        )
    }

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultHazeEffect(
                    hazeState = LocalHazeState.current,
                    tintColor = AconTheme.color.GlassGray900,
                    blurRadius = 20.dp
                )
                .statusBarsPadding()
        ) {
            AconTopBar(
                leadingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.showExitUploadPlaceDialog()
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_x_mark),
                            contentDescription = stringResource(R.string.exit),
                            tint = AconTheme.color.Gray50
                        )
                    }
                },
                content = {
                    Text(
                        text = stringResource(R.string.upload_place_topbar),
                        style = AconTheme.typography.Title4,
                        fontWeight = FontWeight.SemiBold,
                        color = AconTheme.color.White
                    )
                },
                modifier = Modifier
                    .padding(vertical = 14.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(LocalHazeState.current)
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInVertically { height -> height } togetherWith slideOutVertically { height -> -height }
                        } else {
                            slideInVertically { height -> -height } togetherWith slideOutVertically { height -> height }
                        }.using(SizeTransform(clip = true))
                    },
                    label = stringResource(R.string.upload_process_step_transition),
                    contentKey = { it }
                ) { step ->
                    steps.getOrNull(step)?.invoke()
                }
            }
        }

        if (currentStep != steps.lastIndex) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AconFilledTextButton(
                        text = stringResource(R.string.previous),
                        textStyle = AconTheme.typography.Body1.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AconTheme.color.Gray900,
                            contentColor = AconTheme.color.White,
                            disabledContainerColor = AconTheme.color.Gray900,
                            disabledContentColor = AconTheme.color.Gray500
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = AconTheme.color.GlassWhiteDefault
                        ),
                        enabled = state.isPreviousBtnEnabled,
                        onClick = {
                            // TODO - 이전
                            if (currentStep > 0) currentStep--
                        },
                        modifier = Modifier
                            .weight(3f)
                    )

                    AconFilledTextButton(
                        text = stringResource(R.string.next),
                        textStyle = AconTheme.typography.Body1.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AconTheme.color.GlassWhiteDefault,
                            contentColor = AconTheme.color.White,
                            disabledContainerColor = AconTheme.color.GlassWhiteDefault,
                            disabledContentColor = AconTheme.color.Gray500
                        ),
                        enabled = state.isNextBtnEnabled,
                        onClick = {
                            // TODO - 다음
                            if (currentStep < steps.lastIndex) currentStep++
                        },
                        modifier = Modifier
                            .weight(5f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun UploadPlaceScreenPreview() {
    AconTheme {
        UploadPlaceScreen(
            onNavigateBack = {}
        )
    }
}