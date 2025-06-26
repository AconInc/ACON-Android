package com.acon.acon.feature.profile.composable.screen.galleryList.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.dialog.v2.AconTwoActionDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.permission.media.CheckAndRequestMediaPermission
import com.acon.acon.feature.profile.composable.screen.MediaPermissionBottomSheet
import com.acon.acon.feature.profile.composable.screen.galleryList.Album
import com.acon.acon.feature.profile.composable.screen.galleryList.GalleryListUiState
import com.acon.core.ui.compose.getScreenWidth

@Composable
internal fun GalleryListScreen(
    state: GalleryListUiState,
    onBackClicked: () -> Unit,
    onUpdateAllAlbum: () -> Unit,
    onUpdateUserSelectedAlbum: () -> Unit,
    onClickPermissionSettings: (String) -> Unit,
    requestMediaPermission: () -> Unit,
    resetMediaPermission: () -> Unit,
    requestMediaPermissionModal: () -> Unit,
    dismissMediaPermissionModal: () -> Unit,
    requestMediaPermissionDialog: () -> Unit,
    dismissMediaPermissionDialog: () -> Unit,
    onAlbumSelected: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val screenWidthDp = getScreenWidth()
    val dialogWidth = (screenWidthDp * (260f / 360f))

    when (state) {
        is GalleryListUiState.Loading -> {}

        is GalleryListUiState.Granted -> {
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
                            text = stringResource(R.string.my_album),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    modifier = Modifier.padding(vertical = 14.dp)
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

        is GalleryListUiState.Partial -> {
            if (state.requestMediaPermission) {
                CheckAndRequestMediaPermission(
                    onPermissionGranted = {
                        resetMediaPermission()
                        onUpdateAllAlbum()
                    },
                    onPermissionDenied = {
                        resetMediaPermission()
                        requestMediaPermissionDialog()
                    },
                    onPermissionPartial = { onUpdateUserSelectedAlbum() },
                    ignorePartialPermission = false
                )
            }

            if (state.showMediaPermissionModal) {
                MediaPermissionBottomSheet(
                    onDismiss = { dismissMediaPermissionModal() },
                    onClickPermissionCheck = {
                        dismissMediaPermissionModal()
                        requestMediaPermission() // 추가 권한 요청 (권한이 제한된 액세스 허용인 경우 -> 추가 권한 (안드로이드 14+))
                    },
                    onClickPermissionSettings = {
                        dismissMediaPermissionModal()
                        onClickPermissionSettings(context.packageName)
                    }
                )
            }

            if (state.showMediaPermissionDialog) {
                AconTwoActionDialog(
                    title = stringResource(R.string.photo_permission_title),
                    action1 = stringResource(R.string.photo_permission_alert_left_btn),
                    action2 = stringResource(R.string.photo_permission_alert_right_btn),
                    onDismissRequest = { dismissMediaPermissionDialog() },
                    onAction1 = {
                        resetMediaPermission()
                        dismissMediaPermissionDialog()
                    },
                    onAction2 = {
                        resetMediaPermission()
                        dismissMediaPermissionDialog()
                        onClickPermissionSettings(context.packageName)
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.photo_permission_content),
                            color = AconTheme.color.Gray200,
                            style = AconTheme.typography.Body1,
                            maxLines = 1,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                    },
                    modifier = Modifier.width(dialogWidth)
                )
            }

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
                            text = stringResource(R.string.my_album),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    modifier = Modifier.padding(vertical = 14.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.change_photo_permission_call_to_action),
                        color = AconTheme.color.Gray50,
                        style = AconTheme.typography.Body1,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )

                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp, start = 20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.change_photo_permission),
                            color = AconTheme.color.Action,
                            style = AconTheme.typography.Body1,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(8.dp)
                                .noRippleClickable {
                                    resetMediaPermission()
                                    requestMediaPermissionModal()
                                }
                        )
                    }
                }

                if (state.albumList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_photo),
                            color = AconTheme.color.Gray500,
                            style = AconTheme.typography.Body1,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
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
        }

        is GalleryListUiState.Denied -> {
            // 이 화면에 처음들어 왔을 때 권한이 Denied이면 requestMediaPermission 호출
            LaunchedEffect(Unit) {
                requestMediaPermission()
            }

            if (state.requestMediaPermission) {
                CheckAndRequestMediaPermission(
                    onPermissionGranted = {
                        resetMediaPermission()
                        onUpdateAllAlbum()
                    },
                    onPermissionDenied = {
                        resetMediaPermission()
                        requestMediaPermissionDialog()
                    },
                    onPermissionPartial = {
                        onUpdateUserSelectedAlbum()
                    }
                )
            }

            if (state.showMediaPermissionDialog) {
                AconTwoActionDialog(
                    title = stringResource(R.string.photo_permission_title),
                    action1 = stringResource(R.string.photo_permission_alert_left_btn),
                    action2 = stringResource(R.string.photo_permission_alert_right_btn),
                    onDismissRequest = { dismissMediaPermissionDialog() },
                    onAction1 = {
                        resetMediaPermission()
                        dismissMediaPermissionDialog()
                    },
                    onAction2 = {
                        resetMediaPermission()
                        dismissMediaPermissionDialog()
                        onClickPermissionSettings(context.packageName)
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.photo_permission_content),
                            color = AconTheme.color.Gray200,
                            style = AconTheme.typography.Body1,
                            maxLines = 1,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                    },
                    modifier = Modifier.width(dialogWidth)
                )
            }

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
                            text = stringResource(R.string.my_album),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    modifier = Modifier.padding(vertical = 14.dp)
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_photo),
                        color = AconTheme.color.Gray500,
                        style = AconTheme.typography.Body1
                    )
                }
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
    AconTheme {
        GalleryListScreen(
            state = GalleryListUiState.Partial(),
            onBackClicked = {},
            onUpdateAllAlbum = {},
            onUpdateUserSelectedAlbum = {},
            onClickPermissionSettings = {},
            requestMediaPermission = {},
            resetMediaPermission = {},
            requestMediaPermissionModal = {},
            dismissMediaPermissionModal = {},
            requestMediaPermissionDialog = {},
            dismissMediaPermissionDialog = {},
            onAlbumSelected = { _, _ -> }
        )
    }
}