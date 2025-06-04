package com.acon.acon.feature.profile.composable.screen.photoCrop.composable

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.composable.screen.photoCrop.PhotoCropSideEffect
import com.acon.acon.feature.profile.composable.screen.photoCrop.PhotoCropViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PhotoCropContainer(
    photoId: String,
    modifier: Modifier = Modifier,
    viewModel: PhotoCropViewModel = hiltViewModel(),
    onCloseClicked: () -> Unit = {},
    onCompleteSelected: (String) -> Unit = {}
) {
    viewModel.collectSideEffect {
        when (it) {
            PhotoCropSideEffect.NavigateToBack -> {
                onCloseClicked()
            }

            is PhotoCropSideEffect.NavigateToProfileMod -> {
                onCompleteSelected(it.selectedPhotoUri)
            }
        }
    }

    PhotoCropScreen(
        photoId = photoId,
        modifier = modifier,
        onCloseClicked = viewModel::navigateToBack,
        onCompleteSelected = viewModel::navigateToProfileMod
    )
}

@Composable
internal fun PhotoCropScreen(
    photoId: String,
    modifier: Modifier = Modifier,
    onCloseClicked: () -> Unit = {},
    onCompleteSelected: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = AconTheme.color.Gray900)
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center
    ) {

        AconTopBar(
            leadingIcon = {
                IconButton(onClick = onCloseClicked) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_topbar_arrow_left),
                        contentDescription = stringResource(R.string.back),
                    )
                }
            },
            content = {
                Text(
                    text = stringResource(R.string.album),
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.SemiBold,
                    color = AconTheme.color.White
                )
            },
            trailingIcon = {
                TextButton(
                    onClick = { onCompleteSelected(photoId) }
                ) {
                    Text(
                        text = stringResource(R.string.done),
                        style = AconTheme.typography.Title4,
                        fontWeight = FontWeight.SemiBold,
                        color = AconTheme.color.Action
                    )
                }
            }
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val imageWidth = maxWidth
            val imageWidthPx = with(LocalDensity.current) { imageWidth.toPx() }
            val imageHeight = maxHeight
            val imageHeightPx = with(LocalDensity.current) { imageHeight.toPx() }

            val circleRadius = imageWidth / 2
            val circleCenter = Offset(imageWidthPx / 2, imageHeightPx / 2)

            Box(
                modifier = Modifier
                    .width(imageWidth)
                    .height(imageHeight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(Uri.parse(photoId)),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = stringResource(R.string.selected_image),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )

                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawIntoCanvas { canvas ->
                        val path = Path().apply {
                            addRect(Rect(0f, 0f, size.width, size.height))
                            addOval(
                                Rect(
                                    circleCenter.x - circleRadius.toPx(),
                                    circleCenter.y - circleRadius.toPx(),
                                    circleCenter.x + circleRadius.toPx(),
                                    circleCenter.y + circleRadius.toPx()
                                )
                            )
                            fillType = PathFillType.EvenOdd
                        }
                        canvas.drawPath(path, Paint().apply {
                            color = Color.Black.copy(alpha = 0.6f)
                        })
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewPhotoCropScreen() {
    PhotoCropContainer(
        photoId = ""
    )
}