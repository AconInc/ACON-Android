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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
internal fun MenuBoardOverlay(
    imageList: List<String>,
    isMenuBoardLoaded: Boolean,
    onDismiss: () -> Unit
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val zoomState = remember { PinchZoomState() }
    val isZooming = zoomState.isZooming

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AconTheme.color.DimDefault.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            if(!isZooming) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_x_mark),
                    contentDescription = stringResource(R.string.exit),
                    tint = AconTheme.color.Gray50,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 32.dp, start = 14.dp)
                        .noRippleClickable { onDismiss() }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pinchZoomAndTransform(zoomState),
                contentAlignment = Alignment.Center
            ) {
                PinchToZoomImage(
                    menuBoardImage = imageList[currentIndex],
                    isMenuBoardLoaded = isMenuBoardLoaded
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if(!isZooming) {
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
                    }
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }
            }
        }
    }
}

@Composable
internal fun PinchToZoomImage(
    menuBoardImage: String,
    isMenuBoardLoaded: Boolean
) {
    if (isMenuBoardLoaded) {
        Box(
            modifier = Modifier
                .widthIn(max = 230.dp)
                .aspectRatio(230f / 325f),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .crossfade(true)
                    .data(menuBoardImage)
                    .build(),
                contentDescription = stringResource(R.string.menu_board_content_description),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    } else {
        Box(
            modifier = Modifier
                .background(
                    color = AconTheme.color.Gray900,
                    shape = RoundedCornerShape(4.dp)
                )
                .widthIn(max = 230.dp)
                .aspectRatio(230f / 325f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.menu_board_load_failed),
                    color = AconTheme.color.Gray50,
                    style = AconTheme.typography.Title5,
                    fontWeight = FontWeight.SemiBold
                )
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
            onDismiss = {}
        )
    }
}