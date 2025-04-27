package com.acon.acon.feature.profile.composable.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.feature.profile.R

@Composable
fun ProfilePhotoBox(
    modifier: Modifier = Modifier,
    onProfileClicked: () -> Unit = {},
    photoUri: String = "",
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        if (photoUri.isNotBlank()) {
            val imageWidth = maxWidth
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .width(imageWidth)
                    .height(imageWidth)
                    .clip(CircleShape)
                    .noRippleClickable { onProfileClicked() },
                contentAlignment = Alignment.Center
            ) {
                when {
                    photoUri.startsWith("content://") -> {
                        Image(
                            painter = rememberAsyncImagePainter(Uri.parse(photoUri)),
                            contentDescription = "선택한 프로필 사진",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    }

                    photoUri.startsWith("https://") -> {
                        AsyncImage(
                            model = photoUri,
                            contentDescription = "선택한 프로필 사진",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    }
                }
            }
        } else {
            Image(
                imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_default_profile_40),
                contentDescription = stringResource(R.string.content_description_default_profile_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }
    }
}
