package com.acon.feature.profile.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil3.compose.rememberAsyncImagePainter
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.acon.feature.profile.R

@Composable
fun ProfilePhotoBox(
    modifier: Modifier = Modifier,
    photoUri: String = "",
){
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        if (photoUri != ""){
            val imageWidth = maxWidth
            Box(
                modifier = Modifier.fillMaxSize().width(imageWidth).height(imageWidth).clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(Uri.parse(photoUri)),
                    contentDescription = "선택한 프로필 사진",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            }
        } else {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = ImageVector.vectorResource(R.drawable.img_profile_basic_80),
                contentDescription = "Profile Image",
                tint = Color.Unspecified,
            )
        }
    }
}