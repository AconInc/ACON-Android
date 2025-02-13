package com.acon.feature.profile.screen.customGallery.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.core.designsystem.theme.AconTheme
import com.acon.feature.profile.screen.customGallery.Album
import com.acon.feature.profile.screen.customGallery.CustomGalleryState
import com.acon.feature.profile.screen.customGallery.CustomGalleryViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun CustomGalleryContainer(
    modifier : Modifier = Modifier,
    viewModel: CustomGalleryViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
    onAlbumSelected: (String) -> Unit = {},
){
    val state = viewModel.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadAlbums()
    }

    CustomGalleryScreen(
        modifier = modifier,
        state = state,
        albumList = state.albumList,
        onAlbumSelected = viewModel::onAlbumSelected,
        onBackClicked = onBackClicked,
    )
}

@Composable
fun CustomGalleryScreen(
    modifier: Modifier = Modifier,
    state: CustomGalleryState,
    albumList: List<Album>,
    onAlbumSelected: (String) -> Unit,
    onBackClicked: () -> Unit,
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = AconTheme.color.Gray9)
            .statusBarsPadding()
            .navigationBarsPadding()
    ){
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
                    text = "나의 앨범",
                    style = AconTheme.typography.head5_22_sb,
                    color = AconTheme.color.White
                )
            }
        )

        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(albumList) { album ->
                AlbumItem(album = album, onAlbumSelected = onAlbumSelected)
            }
        }
    }
}

@Composable
fun AlbumItem(album: Album, onAlbumSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAlbumSelected(album.id) }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = rememberAsyncImagePainter(album.coverUri),
            contentDescription = "앨범 대표 이미지",
            modifier = Modifier
                .size(64.dp)
                .aspectRatio(1f).clip(RoundedCornerShape(6.dp))
        )
        Column(
            modifier = Modifier
                .weight(1f).fillMaxWidth().padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = album.name,
                style = AconTheme.typography.subtitle1_16_med,
                color = AconTheme.color.White
            )
            Text(
                text = "00000",
                style = AconTheme.typography.body2_14_reg,
                color = AconTheme.color.Gray4
            )
        }
    }
}

@Preview()
@Composable
fun PreviewCustomGalleryScreen(){
    CustomGalleryContainer()
}