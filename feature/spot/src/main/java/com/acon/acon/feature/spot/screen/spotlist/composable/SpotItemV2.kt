package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.v2.SpotV2
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.blur.LocalHazeState
import com.acon.acon.core.designsystem.blur.defaultHazeEffect
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton

@Composable
internal fun SpotItemV2(
    spot: SpotV2,
    modifier: Modifier = Modifier,
    onItemClick: (SpotV2) -> Unit = {},
    onFindWayButtonClick: (spotId: Long) -> Unit = {},
) {

    val textMeasurer = rememberTextMeasurer()
    val measureTextStyle = AconTheme.typography.Body1
    val textLayoutResult = remember {
        textMeasurer.measure(
            text = "+9999",
            style = measureTextStyle,
        )
    }

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
        Column(
            modifier = Modifier.padding(20.dp)
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
                            textLayoutResult.size.width.toDp()
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
        )
    )
}