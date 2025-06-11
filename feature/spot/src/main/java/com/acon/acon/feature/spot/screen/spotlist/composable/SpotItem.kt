package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.component.tag.AconTag
import com.acon.acon.core.designsystem.effect.imageGradientLayer
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.v2.Spot
import com.acon.acon.domain.type.TagType
import com.acon.acon.domain.type.TransportMode

@Composable
internal fun SpotItem(
    spot: Spot,
    transportMode: TransportMode,
    onItemClick: (Spot) -> Unit,
    onFindWayButtonClick: (Spot) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AconTheme.color.GlassWhiteDefault
        ),
        onClick = {
            onItemClick(spot)
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            SpotImage(
                spot = spot,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (spot.image.isNotBlank()) {
                            Modifier.imageGradientLayer()
                        } else {
                            Modifier
                        }
                    )
            )
            SpotInfo(
                spot = spot,
                transportMode = transportMode,
                onFindWayButtonClick = onFindWayButtonClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            )
        }
    }
}

@Composable
private fun SpotInfo(
    spot: Spot,
    transportMode: TransportMode,
    onFindWayButtonClick: (Spot) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (spot.name.length > 15) spot.name.take(15) + stringResource(R.string.ellipsis) else spot.name,
                style = AconTheme.typography.Title4,
                fontWeight = FontWeight.SemiBold,
                color = AconTheme.color.White,
                maxLines = 1,
                modifier = Modifier.padding()
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.acon_line),
                contentDescription = stringResource(R.string.acorn_count_content_description),
                tint = AconTheme.color.Gray50
            )
            Text(
                text = (if (spot.acorn >= 9999) "+" else "") + spot.acorn.coerceAtMost(9999),
                style = AconTheme.typography.Body1,
                fontWeight = FontWeight.W400,
                color = AconTheme.color.White,
                maxLines = 1,
                modifier = Modifier.padding(start = 2.dp),
                textAlign = TextAlign.End
            )
        }
        FlowRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            spot.tags.fastForEach { tagType ->
                AconTag(
                    text = tagType.name.replace("_", " "),
                    backgroundColor = when (tagType) {
                        TagType.NEW -> AconTheme.color.TagNew
                        TagType.LOCAL -> AconTheme.color.TagLocal
                        else -> AconTheme.color.Gray900
                    }
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AconFilledButton(
            modifier = Modifier.align(Alignment.End),
            onClick = {
                onFindWayButtonClick(spot)
            },
            contentPadding = PaddingValues(
                horizontal = 23.dp,
                vertical = 8.dp
            ),
        ) {
            Text(
                text = stringResource(
                    if (transportMode == TransportMode.WALKING) R.string.walking_eta else R.string.biking_eta,
                    spot.eta
                ),
                style = AconTheme.typography.Body1,
                fontWeight = FontWeight.SemiBold,
                color = AconTheme.color.White,
            )
        }
    }
}

@Composable
private fun SpotImage(
    spot: Spot,
    modifier: Modifier = Modifier,
) {
    if (spot.image.isBlank()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_background_no_store),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_acon_fill_white),
                    contentDescription = null,
                    tint = AconTheme.color.GlassWhiteDefault,
                    modifier = Modifier.size(36.dp)
                )

                val emptyDescriptionIds = listOf(
                    R.string.empty_spot_image1,
                    R.string.empty_spot_image2,
                    R.string.empty_spot_image3
                )
                val descriptionId = emptyDescriptionIds.random()
                Text(
                    text = stringResource(descriptionId),
                    style = AconTheme.typography.Body1,
                    fontWeight = FontWeight.SemiBold,
                    color = AconTheme.color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    } else if (LocalInspectionMode.current) {
        Image(
            modifier = modifier,
            painter = painterResource(R.drawable.preview_background_small),
            contentScale = ContentScale.FillHeight,
            contentDescription = null
        )
    } else {
        AsyncImage(
            modifier = modifier,
            model = ImageRequest
                .Builder(LocalContext.current)
                .crossfade(true)
                .data(spot.image)
                .scale(Scale.FILL)
                .build(),
            contentDescription = spot.name,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun SpotGuestItem(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_lock),
            contentDescription = null,
            tint = AconTheme.color.GlassWhiteSelected
        )
        Text(
            text = stringResource(R.string.require_sign_in_for_more),
            style = AconTheme.typography.Body1,
            fontWeight = FontWeight.W400,
            color = AconTheme.color.White,
            modifier = Modifier.padding(top = 10.dp),
        )
        Text(
            text = stringResource(R.string.go_to_sign_in),
            style = AconTheme.typography.Body1,
            fontWeight = FontWeight.SemiBold,
            color = AconTheme.color.Action,
            modifier = Modifier.padding(top = 20.dp),
        )
    }
}

@Composable
@Preview
private fun SpotItemV2Preview() {
    SpotItem(
        spot = Spot(
            id = 1L,
            name = "장소명",
            image = "ddd",
            acorn = 9999,
            eta = 10,
            latitude = 0.0,
            longitude = 0.0,
            tags = emptyList(),
            closingTime = "23:00"
        ),
        transportMode = TransportMode.WALKING,
        onItemClick = {},
        onFindWayButtonClick = {},
        modifier = Modifier
            .height(600.dp)
            .clipToBounds()
    )
}

@Composable
@Preview
private fun SpotItemV2EmptyImagePreview() {
    SpotItem(
        spot = Spot(
            id = 1L,
            name = "장소명",
            image = "",
            acorn = 9999,
            eta = 10,
            latitude = 0.0,
            longitude = 0.0,
            tags = emptyList(),
            closingTime = "23:00"
        ),
        transportMode = TransportMode.WALKING,
        onItemClick = {},
        onFindWayButtonClick = {},
        modifier = Modifier
            .height(600.dp)
            .clipToBounds()
    )
}

@Composable
@Preview
private fun SpotGuestItemPreview() {
    SpotGuestItem(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
    )
}