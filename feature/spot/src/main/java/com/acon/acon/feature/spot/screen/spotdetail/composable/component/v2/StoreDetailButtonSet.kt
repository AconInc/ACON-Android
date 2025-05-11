package com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun StoreDetailButtonSet(
    onClickMenuBoard: () -> Unit,
    onClickShare: () -> Unit,
    onClickMoreOptions: () -> Unit,
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

    Column {
        StoreDetailButton(
            name = "메뉴판",
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
            name = "공유",
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
            name = "더보기",
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
fun StoreDetailButton(
    //@StringRes name: Int, // TODO - 추후 적용
    name: String,
    @DrawableRes imageRes: Int,
    onClickButton: () -> Unit,
    onLongClickButton: (() -> Unit),
    onReleaseAfterLongPress: (() -> Unit)
) {
    var isLongPressed by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(imageRes),
            contentDescription = null,
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isLongPressed = false
                        val pressSucceeded = tryAwaitRelease()
                        if (pressSucceeded) {
                            onReleaseAfterLongPress.invoke()
                        }
                    },
                    onTap = {
                        onClickButton()
                    },
                    onLongPress = {
                        onLongClickButton.invoke()
                    }
                )
            }
        )

        Text(
            text = name,
            color = AconTheme.color.Gray300,
            style = AconTheme.typography.Caption1
        )
    }
}

@Preview
@Composable
private fun StoreButtonSetPreview() {
    AconTheme {
        StoreDetailButtonSet(
            onClickMenuBoard = {},
            onClickShare = {},
            onClickMoreOptions = {}
        )
    }
}