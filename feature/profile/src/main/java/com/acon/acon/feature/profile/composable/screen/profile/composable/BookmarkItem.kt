package com.acon.acon.feature.profile.composable.screen.profile.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.effect.imageGradientLayer
import com.acon.acon.core.designsystem.effect.imageGradientTopLayer
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.v2.SavedSpot

@Composable
internal fun BookmarkItem(
    spot: SavedSpot, // TODO - 서버에서 보내준 값으로 변경 (state)
    onClickSpotItem:() -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .noRippleClickable { onClickSpotItem() }
    ) {
        if(spot.image?.isEmpty() == true) {
            SubcomposeAsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .crossfade(true)
                    .data(spot.image)
                    .scale(Scale.FIT)
                    .build(),
                error = {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(AconTheme.color.GlassWhiteDisabled)
                            .imageGradientLayer()
                    ) {
                        Text(
                            text = if (spot.name.length > 9) spot.name.take(8) + stringResource(R.string.ellipsis) else spot.name,
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
                            text = stringResource(R.string.image_load_failed),
                            color = AconTheme.color.Gray50,
                            style = AconTheme.typography.Caption1,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                },
                contentDescription = stringResource(R.string.store_background_image_content_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .imageGradientTopLayer()
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
                contentDescription = stringResource(R.string.no_store_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .imageGradientTopLayer()
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

@Preview
@Composable
private fun LoadFailedBookmarkItemPreview() {
    AconTheme {
        BookmarkItem(
            spot = SavedSpot(1, "", ""),
            onClickSpotItem = {}
        )
    }
}