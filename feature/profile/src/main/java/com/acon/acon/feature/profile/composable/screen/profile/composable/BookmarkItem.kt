package com.acon.acon.feature.profile.composable.screen.profile.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.effect.imageGradientLayer
import com.acon.acon.core.designsystem.effect.imageGradientTopLayer
import com.acon.acon.core.designsystem.image.rememberDefaultLoadImageErrorPainter
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.profile.SavedSpot

@Composable
internal fun BookmarkItem(
    spot: SavedSpot,
    onClickSpotItem:() -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .noRippleClickable { onClickSpotItem() }
    ) {
        if(spot.image.isNotEmpty()) {
            AsyncImage(
                model = spot.image,
                contentDescription = stringResource(R.string.store_background_image_content_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .imageGradientTopLayer(),
                error = rememberDefaultLoadImageErrorPainter()
            )

            Text(
                text = if (spot.name.length > 9) spot.name.take(8) + stringResource(R.string.ellipsis) else spot.name,
                color = AconTheme.color.White,
                style = AconTheme.typography.Title5,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_bg_no_store_profile),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .imageGradientLayer()
            )

            Text(
                text =  if (spot.name.length > 9) spot.name.take(8) + stringResource(R.string.ellipsis) else spot.name,
                color = AconTheme.color.White,
                style = AconTheme.typography.Title5,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
            )

            Text(
                text = stringResource(R.string.no_store_image),
                color = AconTheme.color.Gray50,
                style = AconTheme.typography.Caption1,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun BookmarkItemPreview() {
    AconTheme {
        BookmarkItem(
            spot = SavedSpot(1, "", ""),
            onClickSpotItem = {}
        )
    }
}