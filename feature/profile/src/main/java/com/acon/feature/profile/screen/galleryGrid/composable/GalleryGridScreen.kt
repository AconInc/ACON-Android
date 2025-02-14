package com.acon.feature.profile.screen.galleryGrid.composable

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.core.designsystem.theme.AconTheme
import com.acon.feature.profile.screen.galleryGrid.GalleryGridSideEffect
import com.acon.feature.profile.screen.galleryGrid.GalleryGridState
import com.acon.feature.profile.screen.galleryGrid.GalleryGridViewModel
import com.acon.feature.profile.screen.profileMod.ProfileModSideEffect
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun GalleryGridContainer(
    modifier : Modifier = Modifier,
    viewModel: GalleryGridViewModel = hiltViewModel(),
    albumId: String,
    albumName: String,
    onBackClicked: () -> Unit = {},
    onConfirmSelected: (String) -> Unit = {},
){
    val state = viewModel.collectAsState().value

    LaunchedEffect(albumId) {
        viewModel.loadPhotos(albumId)

        //SideEffect를 안쓰고 넘기면 되네??...
    }

    GalleryGridScreen(
        modifier = modifier,
        state = state,
        albumName = albumName,
        onPhotoSelected = viewModel::onPhotoSelected,
        onConfirmSelected = onConfirmSelected,
        onBackClicked = onBackClicked,
    )
}

@Composable
fun GalleryGridScreen(
    modifier: Modifier = Modifier,
    state: GalleryGridState,
    albumName: String,
    onPhotoSelected: (Uri) -> Unit,
    onConfirmSelected: (String) -> Unit,
    onBackClicked: () -> Unit
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
                IconButton(onClick = onBackClicked) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = com.acon.core.designsystem.R.drawable.ic_arrow_left_28),
                        contentDescription = "Back",
                    )
                }
            },
            content = {
                Text(
                    text = albumName,
                    style = AconTheme.typography.head5_22_sb,
                    color = AconTheme.color.White
                )
            },
            trailingIcon = {
                TextButton(
                    onClick = { state.selectedPhotoUri?.let { onConfirmSelected(it.toString()) } }
                ) {
                    Text(
                        text = "선택",
                        style = AconTheme.typography.subtitle1_16_med,
                        color = if (state.selectedPhotoUri != null) AconTheme.color.White else AconTheme.color.Gray5
                    )
                }
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(state.photoList) { photoUri ->
                PhotoItem(
                    uri = photoUri,
                    isSelected = photoUri == state.selectedPhotoUri,
                    onClick = { onPhotoSelected(photoUri) }
                )
            }
        }
    }
}

@Composable
fun PhotoItem(
    uri: Uri,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.padding(2.dp).size(87.dp).clickable{ onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(uri),
            contentDescription = "사진",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AconTheme.color.Dim_g_30)
            )
        }
    }
}

