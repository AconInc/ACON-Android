package com.acon.acon.feature.profile.composable.screen.galleryList.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.composable.screen.galleryList.Album
import com.acon.acon.feature.profile.composable.screen.galleryList.GalleryListSideEffect
import com.acon.acon.feature.profile.composable.screen.galleryList.GalleryListState
import com.acon.acon.feature.profile.composable.screen.galleryList.GalleryListViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun GalleryListContainer(
    modifier: Modifier = Modifier,
    viewModel: GalleryListViewModel = hiltViewModel(),
    onAlbumSelected: (String, String) -> Unit = { _, _ -> },
    onBackClicked: () -> Unit = {},
) {
    val state by viewModel.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAlbums()
    }

    viewModel.collectSideEffect {
        when (it) {
            is GalleryListSideEffect.NavigateToAlbumGrid -> {
                onAlbumSelected(it.albumId, it.albumName)
            }
        }
    }

    GalleryListScreen(
        state = state,
        modifier = modifier,
        onAlbumSelected = viewModel::onClickAlbum,
        onBackClicked = onBackClicked
    )
}

@Composable
internal fun GalleryListScreen(
    state: GalleryListState,
    modifier: Modifier = Modifier,
    onAlbumSelected: (String, String) -> Unit,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = AconTheme.color.Gray900)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        AconTopBar(
            leadingIcon = {
                IconButton(onClick = onBackClicked) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_topbar_arrow_left),
                        contentDescription = stringResource(R.string.back)
                    )
                }
            },
            content = {
                Text(
                    text = stringResource(R.string.my_album),
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.SemiBold,
                    color = AconTheme.color.White
                )
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = state.albumList,
                key = { album -> album.id }
            ) { album ->
                AlbumItem(
                    album = album,
                    onAlbumSelected = onAlbumSelected
                )
            }
        }
    }
}

@Composable
private fun AlbumItem(
    album: Album,
    onAlbumSelected: (String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onAlbumSelected(album.id, album.name) }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = rememberAsyncImagePainter(album.coverUri),
            contentDescription = stringResource(R.string.content_description_thumbnail),
            modifier = Modifier
                .size(64.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(6.dp))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 16.dp)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = album.name,
                style = AconTheme.typography.Title4,
                color = AconTheme.color.White
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = album.imageCount.toString(),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Gray500
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCustomGalleryScreen() {
    GalleryListContainer()
}