package com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun MenuBoardOverlay(
    imageList: List<Int>,
    onDismiss: () -> Unit = {}
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val zoomState = remember { PinchZoomState() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable {
                        if (!zoomState.isZooming) {
                            onDismiss()
                        }
                    }
            )

            PinchToZoomImage(
                zoomState = zoomState,
                imageResId = imageList[currentIndex]
            )

            if (!zoomState.isZooming) {
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
                            contentDescription = "이전 이미지",
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
                            contentDescription = "다음 이미지",
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
}

@Composable
fun PinchToZoomImage(
    zoomState: PinchZoomState,
    @DrawableRes imageResId: Int,
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 230.dp)
                .aspectRatio(230f / 325f)
                .pinchZoomAndTransform(zoomState),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(imageResId),
                contentDescription = "메뉴판 이미지",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview
@Composable
private fun MenuBoardOverlayPreview() {
    val imageList = listOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_error_1_120,
    )
    AconTheme {
        MenuBoardOverlay(
            imageList = imageList,
        )
    }
}