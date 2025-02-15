package com.acon.feature.profile.screen.photoCrop.composable

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.acon.core.designsystem.R
import com.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.core.designsystem.theme.AconColor
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
                        color = AconTheme.color.White
                    )
                }
            }
        )

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
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
                    .height(imageHeight)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(Uri.parse(photoId)),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "선택한 프로필 사진",
                    contentScale = ContentScale.Crop
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
private fun PreviewPhotoCropScreen(){
    PhotoCropContainer()
}