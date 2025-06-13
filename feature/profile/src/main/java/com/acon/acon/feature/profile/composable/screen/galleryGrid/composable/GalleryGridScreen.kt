package com.acon.acon.feature.profile.composable.screen.galleryGrid.composable

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.composable.screen.galleryGrid.GalleryGridUiState

@Composable
internal fun GalleryGridScreen(
    state: GalleryGridUiState,
    albumName: String,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onUpdateAllImages: () -> Unit,
    onUpdateUserSelectedImages: () -> Unit,
    onClickPermissionSettings: (String) -> Unit,
    requestMediaPermission: () -> Unit,
    resetMediaPermission: () -> Unit,
    requestMediaPermissionModal: () -> Unit,
    dismissMediaPermissionModal: () -> Unit,
    requestMediaPermissionDialog: () -> Unit,
    dismissMediaPermissionDialog: () -> Unit,
    onPhotoSelected: (Uri) -> Unit,
    onConfirmSelected: (String) -> Unit
) {
    // TODO - state 처리, UI 및 로직 구현
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = AconTheme.color.Gray900)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        AconTopBar(
            leadingIcon = {
                IconButton(
                    onClick = { onBackClicked() }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_topbar_arrow_left),
                        contentDescription = stringResource(R.string.back),
                        tint = AconTheme.color.Gray50
                    )
                }
            },
            content = {
                Text(
                    text = albumName,
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.SemiBold,
                    color = AconTheme.color.White
                )
            },
            trailingIcon = {
                if (true) {
                    Text(
                        text = stringResource(R.string.select),
                        style = AconTheme.typography.Title4,
                        fontWeight = FontWeight.SemiBold,
                        color = AconTheme.color.Action,
                        modifier = Modifier.noRippleClickable {
                            //onConfirmSelected(state.selectedPhotoUri.toString())
                        }
                    )
                }
            },
            modifier = Modifier.padding(vertical = 14.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
//            items(
//                items = state.photoList,
//                key = { photoUri -> photoUri }
//            ) { photoUri ->
//                PhotoItem(
//                    uri = photoUri,
//                    isSelected = photoUri == state.selectedPhotoUri,
//                    onClick = { onPhotoSelected(photoUri) }
//                )
//            }
        }
    }
}

@Composable
private fun PhotoItem(
    uri: Uri,
    columns: Int = 4,
    spacing: Int = 4,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val itemSize = (screenWidthDp - ((columns - 1) * spacing)) / columns

    Box(
        modifier = Modifier
            .size(itemSize.dp)
            .noRippleClickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(uri),
            contentDescription = stringResource(R.string.content_description_photo),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AconTheme.color.Action.copy(alpha = 0.2f))
            )
        }
    }
}