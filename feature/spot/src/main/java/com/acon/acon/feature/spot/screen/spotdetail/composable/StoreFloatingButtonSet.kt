package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
internal fun StoreFloatingButtonSet(
    onClickMenuBoard: () -> Unit,
    onClickShare: () -> Unit,
    onClickMoreOptions: () -> Unit,
    modifier: Modifier = Modifier,
    isMenuBoarEnabled: Boolean = false,
) {
    var isMenuBoardLongPressed by remember { mutableStateOf(false) }
    var isShareLongPressed by remember { mutableStateOf(false) }
    var isMoreOptionsLongPressed by remember { mutableStateOf(false) }

    val menuBoardImage = when {
        isMenuBoardLongPressed -> R.drawable.ic_menu_board_pressed
        isMenuBoarEnabled -> R.drawable.ic_menu_board_enable
        else -> R.drawable.ic_menu_board_disable
    }
    val shareImage =
        if (isShareLongPressed) R.drawable.ic_share_pressed else R.drawable.ic_share_enable
    val moreOptionsImage =
        if (isMoreOptionsLongPressed) R.drawable.ic_ellipsis_pressed else R.drawable.ic_ellipsis_enable

    Column(
        modifier = modifier
    ) {
        StoreDetailButton(
            name = stringResource(R.string.floating_btn_menu_board),
            imageRes = menuBoardImage,
            onClickButton = onClickMenuBoard,
            onLongClickButton = { isMenuBoardLongPressed = true },
            onReleaseAfterLongPress = {
                onClickMenuBoard()
                isMenuBoardLongPressed = false
            }
        )

        Spacer(Modifier.height(36.dp))
        StoreDetailButton(
            name = stringResource(R.string.floating_btn_share),
            imageRes = shareImage,
            onClickButton = onClickShare,
            onLongClickButton = { isShareLongPressed = true },
            onReleaseAfterLongPress = {
                onClickShare()
                isMenuBoardLongPressed = false
            }
        )

        Spacer(Modifier.height(36.dp))
        StoreDetailButton(
            name = stringResource(R.string.floating_btn_more_option),
            imageRes = moreOptionsImage,
            onClickButton = onClickMoreOptions,
            onLongClickButton = { isMoreOptionsLongPressed = true },
            onReleaseAfterLongPress = {
                onClickMoreOptions()
                isMoreOptionsLongPressed = false
            }
        )
    }
}

@Composable
internal fun StoreDetailButton(
    name: String,
    @DrawableRes imageRes: Int,
    onClickButton: () -> Unit,
    onLongClickButton: (() -> Unit),
    onReleaseAfterLongPress: (() -> Unit)
) {
    var isLongPressed by remember { mutableStateOf(false) }
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val throttleTime = 1000L

    val density = LocalDensity.current
    val blurPx = with(density) { 4.dp.toPx() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Icon(
                imageVector = ImageVector.vectorResource(imageRes),
                contentDescription = null,
                Modifier
                    .offset(y = (4).dp)
                    .blur(4.dp),
                tint = AconTheme.color.Black.copy(0.16f),
            )
            Image(
                imageVector = ImageVector.vectorResource(imageRes),
                contentDescription = name,
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isLongPressed = false
                                val pressSucceeded = tryAwaitRelease()
                                if (pressSucceeded && isLongPressed) {
                                    onReleaseAfterLongPress()
                                }
                            },
                            onTap = {
                                val currentTime = System.currentTimeMillis()
                                if (currentTime - lastClickTime >= throttleTime) {
                                    lastClickTime = currentTime
                                    onClickButton()
                                }
                            },
                            onLongPress = {
                                isLongPressed = true
                                onLongClickButton()
                            }
                        )
                    }
            )
        }

        Text(
            text = name,
            color = AconTheme.color.Gray50,
            style = AconTheme.typography.Caption1.copy(
                shadow = Shadow(
                    color = AconTheme.color.Black.copy(0.16f),
                    offset = Offset(x = 0f, y = blurPx),
                    blurRadius = blurPx
                )
            )
        )
    }
}

@Preview
@Composable
private fun StoreButtonSetPreview() {
    AconTheme {
        StoreFloatingButtonSet(
            onClickMenuBoard = {},
            onClickShare = {},
            onClickMoreOptions = {}
        )
    }
}