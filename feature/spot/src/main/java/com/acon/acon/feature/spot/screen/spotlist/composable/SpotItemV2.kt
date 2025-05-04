package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.blur.LocalHazeState
import com.acon.acon.core.designsystem.blur.defaultHazeEffect
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.v2.SpotV2
import com.acon.feature.common.compose.getTextSizeDp

@Composable
internal fun SpotItemV2(
    spot: SpotV2,
    onItemClick: (SpotV2) -> Unit,
    onFindWayButtonClick: (spotId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AconTheme.color.Gray200
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
                modifier = Modifier.fillMaxSize()
            )
            VerticalGradient()
            SpotInfo(
                spot = spot,
                onFindWayButtonClick = onFindWayButtonClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun SpotInfo(
    spot: SpotV2,
    onFindWayButtonClick: (spotId: Long) -> Unit,
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
                text = spot.name,
                style = AconTheme.typography.Title4,
                fontWeight = FontWeight.SemiBold,
                color = AconTheme.color.White,
                modifier = Modifier.padding()
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.acon_line),
                contentDescription = stringResource(R.string.dotori_count_content_description),
                tint = AconTheme.color.Gray50
            )
            Text(
                text = spot.dotori,
                style = AconTheme.typography.Body1,
                fontWeight = FontWeight.W400,
                color = AconTheme.color.White,
                modifier = Modifier.padding(start = 2.dp).width(
                    with(LocalDensity.current) {
                        getTextSizeDp("+9999", AconTheme.typography.Body1).width
                    }
                ),
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AconFilledButton(
            modifier = Modifier.align(Alignment.End).defaultHazeEffect(
                hazeState = LocalHazeState.current,
                tintColor = AconTheme.color.GlassWhiteDefault
            ),
            onClick = {
                onFindWayButtonClick(spot.id)
            },
            contentPadding = PaddingValues(
                horizontal = 23.dp,
                vertical = 8.dp
            ),
        ) {
            Text(
                text = "${spot.walkingTime} ${stringResource(R.string.find_way)}",
                style = AconTheme.typography.Body1,
                fontWeight = FontWeight.SemiBold,
                color = AconTheme.color.White,
            )
        }
    }
}

@Composable
private fun SpotImage(
    spot: SpotV2,
    modifier: Modifier = Modifier,
) {
    if (LocalInspectionMode.current) {
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
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun BoxScope.VerticalGradient() {
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .fillMaxHeight(.25f)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        AconTheme.color.Gray900,
                        AconTheme.color.Gray900.copy(alpha = .0f),
                    )
                )
            )
    )
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .fillMaxHeight(.25f)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        AconTheme.color.Gray900.copy(alpha = .0f),
                        AconTheme.color.Gray900
                    )
                )
            )
    )
}

@Composable
@Preview
private fun SpotItemV2Preview() {
    SpotItemV2(
        spot = SpotV2(
            id = 1L,
            name = "장소명",
            image = "",
            dotori = "+9999",
            walkingTime = "도보 10분"
        ),
        onItemClick = {},
        onFindWayButtonClick = {},
        modifier = Modifier.height(600.dp).clipToBounds()
    )
}