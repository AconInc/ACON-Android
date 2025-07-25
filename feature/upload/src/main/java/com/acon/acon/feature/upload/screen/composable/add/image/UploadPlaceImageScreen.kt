package com.acon.acon.feature.upload.screen.composable.add.image

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import coil3.compose.AsyncImage
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.effect.imageGradientBottomLayer
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.ui.compose.getScreenHeight
import com.acon.acon.core.ui.compose.getScreenWidth

private const val maxImageCount = 10

@Composable
internal fun UploadPlaceImageScreen(

) {
    // TODO - 임시변수
    val selectedUris = remember { mutableStateListOf<Uri>() }

    val screenWidthDp = getScreenWidth()
    val screenHeightDp = getScreenHeight()
    val imageBoxHeight = screenHeightDp * 0.4f

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        val currentSize = selectedUris.size
        val canAddCount = maxImageCount - currentSize
        val toAdd = uris.take(canAddCount)
        selectedUris.addAll(toAdd)
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            if (selectedUris.size < maxImageCount) selectedUris.size + 1
            else selectedUris.size
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray900)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.required_field),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray300,
            modifier = Modifier.padding(top = 40.dp)
        )

        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.upload_place_image_title),
            style = AconTheme.typography.Headline3,
            color = AconTheme.color.White,
            modifier = Modifier.padding(2.dp)
        )

        Spacer(Modifier.height(32.dp))
        if (selectedUris.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageBoxHeight)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AconTheme.color.GlassWhiteDisabled)
                    .aspectRatio(1f)
                    .padding(horizontal = 8.dp)
                    .noRippleClickable {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                    contentDescription = stringResource(R.string.content_description_upload_place_image),
                    tint = AconTheme.color.Gray50,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageBoxHeight)
                    .padding(horizontal = 8.dp)
            ) { page ->
                if (page < selectedUris.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .aspectRatio(1f)
                            .padding(horizontal = 4.dp)
                            .imageGradientBottomLayer()
                    ) {
                        AsyncImage(
                            model = selectedUris[page],
                            contentDescription = stringResource(R.string.content_description_select_upload_place_image),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                            contentDescription = stringResource(R.string.content_description_delete_uploaded_place_image),
                            tint = AconTheme.color.Action,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp, bottom = 16.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(AconTheme.color.GlassWhiteDisabled)
                            .aspectRatio(1f)
                            .noRippleClickable {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                            contentDescription = stringResource(R.string.content_description_upload_place_image),
                            tint = AconTheme.color.Gray50,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun UploadPlaceImageScreenPreview() {
    AconTheme {
        UploadPlaceImageScreen()
    }
}

