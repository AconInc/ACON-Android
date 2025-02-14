package com.acon.feature.profile.screen.photoCrop.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.core.designsystem.R
import com.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.core.designsystem.theme.AconTheme
import com.acon.feature.profile.screen.photoCrop.PhotoCropState
import com.acon.feature.profile.screen.photoCrop.PhotoCropViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun PhotoCropContainer(
    modifier : Modifier = Modifier,
    viewModel: PhotoCropViewModel = hiltViewModel(),
    photoId: String = "",
    onCloseClicked: () -> Unit = {},
    onCompleteSelected: (String) -> Unit = {}
){
    val state = viewModel.collectAsState().value

    PhotoCropScreen(
        modifier = modifier,
        state = state,
        photoId = photoId,
        onCloseClicked = onCloseClicked,
        onCompleteSelected = onCompleteSelected
    )
}

@Composable
fun PhotoCropScreen(
    modifier: Modifier = Modifier,
    state: PhotoCropState,
    photoId: String,
    onCloseClicked: () -> Unit = {},
    onCompleteSelected: (String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = AconTheme.color.Gray9)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        AconTopBar(
            leadingIcon = {
                IconButton(onClick = onCloseClicked) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_dissmiss_28),
                        contentDescription = "Close",
                    )
                }
            },
            content = {
                Text(
                    text = "앨범",
                    style = AconTheme.typography.head5_22_sb,
                    color = AconTheme.color.White
                )
            },
            trailingIcon = {
                TextButton(
                    onClick = { onCompleteSelected(photoId) }
                ) {
                    Text(
                        text = "완료",
                        style = AconTheme.typography.subtitle1_16_med,
                        color = AconTheme.color.Gray5
                    )
                }
            }
        )

        Image(
            imageVector = ImageVector.vectorResource(com.acon.feature.profile.R.drawable.img_profile_basic_80),
            contentDescription = "프로필 크롭 영역"
        )
    }
}

@Preview
@Composable
private fun PreviewPhotoCropScreen(){
    PhotoCropContainer()
}