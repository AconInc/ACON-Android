package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import coil3.compose.AsyncImage
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import dev.chrisbanes.haze.hazeSource

@Composable
internal fun MenuBoardOverlay(
    imageList: List<Int>,
    isMenuBoardLoaded: Boolean,
    refreshMenuBoard: () -> Unit,
    onDismiss: () -> Unit
) {
    val dialogWindowProvider = LocalView.current.parent as? DialogWindowProvider
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val zoomState = remember { PinchZoomState() }

    var menuBoardQA by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        dialogWindowProvider?.window?.setDimAmount(0f)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AconTheme.color.DimDefault)
                .hazeSource(LocalHazeState.current),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_x_mark),
                contentDescription = stringResource(R.string.exit),
                tint = AconTheme.color.Gray50,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 32.dp, start = 14.dp)
                    .noRippleClickable {
                        menuBoardQA = !menuBoardQA // QA용 코드
                        //onDismiss()
                    }
            )

            PinchToZoomImage(
                zoomState = zoomState,
                menuBoardImage = imageList[currentIndex],
                isMenuBoardLoaded = menuBoardQA, //isMenuBoardLoaded
                refreshMenuBoard = refreshMenuBoard
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (imageList.size > 1 && currentIndex > 0) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_menu_arrow_back),
                        contentDescription = stringResource(R.string.btn_previous_background_image_content_description),
                        modifier = Modifier
                            .size(36.dp)
                            .noRippleClickable(enabled = !zoomState.isZooming) {
                                currentIndex--
                            },
                        tint = AconTheme.color.Gray50
                    )
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                if (imageList.size > 1 && currentIndex < imageList.lastIndex) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_menu_arrow_forward),
                        contentDescription = stringResource(R.string.btn_next_image_content_description),
                        modifier = Modifier
                            .size(36.dp)
                            .noRippleClickable(enabled = !zoomState.isZooming) {
                                currentIndex++
                            },
                        tint = AconTheme.color.Gray50
                    )
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }
            }
        }
    }
}

@Composable
internal fun PinchToZoomImage(
    zoomState: PinchZoomState,
    menuBoardImage: Int,
    isMenuBoardLoaded: Boolean,
    refreshMenuBoard: () -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) } // TODO - 뷰모델에서 관리하도록 추후 수정

    if (isMenuBoardLoaded) {
        Box(
            modifier = Modifier
                .widthIn(max = 230.dp)
                .aspectRatio(230f / 325f)
                .pinchZoomAndTransform(zoomState),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = menuBoardImage,
                contentDescription = stringResource(R.string.menu_board_content_description),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    } else {
        Box(
            modifier = Modifier
                .background(
                    color = AconTheme.color.GlassWhiteDisabled,
                    shape = RoundedCornerShape(4.dp)
                )
                .defaultHazeEffect(
                    hazeState = LocalHazeState.current,
                    tintColor = AconTheme.color.GlassWhiteDisabled,
                    backgroundColor = AconTheme.color.DimDefault
                )
                .widthIn(max = 230.dp)
                .aspectRatio(230f / 325f),
            contentAlignment = Alignment.Center
        ) {
            if (false) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .noRippleClickable {
                            refreshMenuBoard()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(R.string.menu_board_load_failed),
                        color = AconTheme.color.Gray50,
                        style = AconTheme.typography.Title5,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = stringResource(R.string.retry),
                        color = AconTheme.color.Action,
                        style = AconTheme.typography.Body1,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MenuBoardOverlayPreview() {
    AconTheme {
        MenuBoardOverlay(
            imageList = emptyList(),
            isMenuBoardLoaded = false,
            refreshMenuBoard = {},
            onDismiss = {}
        )
    }
}