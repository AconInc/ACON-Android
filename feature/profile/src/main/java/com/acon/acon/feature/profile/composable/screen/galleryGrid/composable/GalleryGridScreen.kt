package com.acon.acon.feature.profile.composable.screen.galleryGrid.composable

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.dialog.v2.AconTwoActionDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.core.ui.permission.media.CheckAndRequestMediaPermission
import com.acon.acon.feature.profile.composable.screen.MediaPermissionBottomSheet
import com.acon.acon.feature.profile.composable.screen.galleryGrid.GalleryGridUiState
import com.acon.core.ui.compose.getScreenWidth
import kotlin.math.roundToInt

@Composable
internal fun GalleryGridScreen(
    state: GalleryGridUiState,
    albumName: String,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onLoadMoreImage: () -> Unit,
    onUpdateAllPhotos: () -> Unit,
    onUpdateUserSelectedPhotos: () -> Unit,
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
    val context = LocalContext.current
    val screenWidthDp = getScreenWidth()
    val dialogWidth = (screenWidthDp * (260f / 360f))

    val listState = rememberLazyGridState()

    when(state) {
        is GalleryGridUiState.Loading -> {}

        is GalleryGridUiState.Granted -> {
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
                        if (state.selectedPhotoUri != null) {
                            Text(
                                text = stringResource(R.string.select),
                                style = AconTheme.typography.Title4,
                                fontWeight = FontWeight.SemiBold,
                                color = AconTheme.color.Action,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .noRippleClickable {
                                        onConfirmSelected(state.selectedPhotoUri.toString())
                                    }
                            )
                        }
                    },
                    modifier = Modifier.padding(vertical = 14.dp)
                )

                EndlessLazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxSize(),
                    listState = listState,
                    loading = false,
                    loadMore = { onLoadMoreImage() },
                    loadingItem = {}
                ) {
                    itemsIndexed(
                        items = state.photoList,
                        key = { index, uri -> "$index-$uri" }
                    ) { ndex, photoUri ->
                        PhotoItem(
                            uri = photoUri,
                            isSelected = photoUri == state.selectedPhotoUri,
                            onClick = { onPhotoSelected(photoUri) }
                        )
                    }
                }
            }
        }

        is GalleryGridUiState.Partial -> {
            if (state.requestMediaPermission) {
                CheckAndRequestMediaPermission(
                    onPermissionGranted = {
                        resetMediaPermission()
                        onUpdateAllPhotos()
                    },
                    onPermissionDenied = {
                        resetMediaPermission()
                        requestMediaPermissionDialog()
                    },
                    onPermissionPartial = { onUpdateUserSelectedPhotos() },
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
                            text = albumName,
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    trailingIcon = {
                        if (state.selectedPhotoUri != null) {
                            Text(
                                text = stringResource(R.string.select),
                                style = AconTheme.typography.Title4,
                                fontWeight = FontWeight.SemiBold,
                                color = AconTheme.color.Action,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .noRippleClickable {
                                        onConfirmSelected(state.selectedPhotoUri.toString())
                                    }
                            )
                        }
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

                if (state.photoList.isEmpty()) {
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
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = state.photoList,
                            key = { photoUri -> photoUri }
                        ) { photoUri ->
                            PhotoItem(
                                uri = photoUri,
                                isSelected = photoUri == state.selectedPhotoUri,
                                onClick = { onPhotoSelected(photoUri) }
                            )
                        }
                    }
                }
            }
        }

        is GalleryGridUiState.Denied -> {
            // 이 화면에 처음들어 왔을 때 권한이 Denied이면 requestMediaPermission 호출
            LaunchedEffect(Unit) {
                requestMediaPermission()
            }

            if (state.requestMediaPermission) {
                CheckAndRequestMediaPermission(
                    onPermissionGranted = {
                        resetMediaPermission()
                        onUpdateAllPhotos()
                    },
                    onPermissionDenied = {
                        resetMediaPermission()
                        requestMediaPermissionDialog()
                    },
                    onPermissionPartial = { onUpdateUserSelectedPhotos() }
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
private fun PhotoItem(
    uri: Uri,
    columns: Int = 4,
    spacing: Int = 4,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthDp = configuration.screenWidthDp
    val itemSizeDp = ((screenWidthDp - ((columns - 1) * spacing)) / columns).dp
    val itemSizePx = with(density) { itemSizeDp.toPx().roundToInt() }

    Box(
        modifier = Modifier
            .size(itemSizeDp)
            .noRippleClickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .size(itemSizePx)
                .crossfade(true)
                .build(),
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

@Preview
@Composable
private fun PreviewGalleryGridScreen() {
    AconTheme {
        GalleryGridScreen(
            state = GalleryGridUiState.Partial(),
            albumName = "",
            onBackClicked = {},
            onLoadMoreImage = {},
            onUpdateAllPhotos = {},
            onUpdateUserSelectedPhotos = {},
            onClickPermissionSettings = {},
            requestMediaPermission = {},
            resetMediaPermission = {},
            requestMediaPermissionModal = {},
            dismissMediaPermissionModal = {},
            requestMediaPermissionDialog = {},
            dismissMediaPermissionDialog = {},
            onPhotoSelected = { },
            onConfirmSelected = { }
        )
    }
}