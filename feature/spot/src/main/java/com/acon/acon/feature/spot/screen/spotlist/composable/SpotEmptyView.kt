package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.acon.acon.core.common.UrlConstants
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.toast.showToast
import com.acon.acon.domain.model.spot.v2.Spot
import com.acon.acon.domain.type.TransportMode
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.spot.mock.spotListUiStateRestaurantMock
import com.acon.feature.common.compose.LocalRequestSignIn
import com.acon.feature.common.compose.getScreenHeight
import com.acon.feature.common.compose.toDp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val MAX_GUEST_AVAILABLE_COUNT = 5

@Composable
internal fun SpotEmptyView(
    userType: UserType,
    otherSpots: ImmutableList<Spot>,
    onSpotClick: (Spot, rank: Int) -> Unit,
    onTryFindWay: (Spot) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenHeightDp = getScreenHeight()
    val screenHeightPx = with(LocalDensity.current) {
        screenHeightDp.toPx()
    }

    val itemHeightPx by remember {
        mutableFloatStateOf(screenHeightPx * .3f)
    }

    val onSignInRequired = LocalRequestSignIn.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.no_available_spots),
            style = AconTheme.typography.Title2,
            color = AconTheme.color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 24.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.apologize_for_no_spots),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray500,
            fontWeight = FontWeight.W400,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (otherSpots.isNotEmpty()) {
            otherSpots.fastForEachIndexed { index, spot ->
                if (index == 0)
                    Text(
                        text = stringResource(R.string.recommend_other_spots),
                        style = AconTheme.typography.Title4,
                        color = AconTheme.color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 60.dp, bottom = 24.dp)
                    )
                if (index >= MAX_GUEST_AVAILABLE_COUNT && userType == UserType.GUEST) {
                    SpotGuestItem(
                        spot = spot,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .height(itemHeightPx.toDp())
                            .fillMaxWidth(),
                        onItemClick = { onSignInRequired(null) }
                    )
                } else {
                    SpotItem(
                        spot = spot,
                        transportMode = TransportMode.BIKING,
                        onItemClick = { onSpotClick(spot, index + 1) },
                        onFindWayButtonClick = onTryFindWay,
                        modifier = Modifier
                            .height(itemHeightPx.toDp())
                            .padding(bottom = 12.dp)
                            .fillMaxWidth(),
                    )
                }
            }
        } else {
            val uriHandler = LocalUriHandler.current
            val context = LocalContext.current

            Text(
                text = stringResource(R.string.no_other_spots),
                style = AconTheme.typography.Title4,
                color = AconTheme.color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 60.dp, bottom = 24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.ask_for_new_spot),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Action,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.noRippleClickable {
                    try {
                        uriHandler.openUri(UrlConstants.REQUEST_NEW_SPOT)
                    } catch (e: Exception) {
                        context.showToast("웹사이트 접속에 실패했어요")
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun SpotListEmptyView1Preview() {
    SpotEmptyView(
        userType = UserType.GUEST,
        otherSpots = spotListUiStateRestaurantMock.spotList.toImmutableList(),
        onSpotClick = { _, _ -> },
        onTryFindWay = {},
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
private fun SpotListEmptyView2Preview() {
    SpotEmptyView(
        userType = UserType.GUEST,
        otherSpots = listOf<Spot>().toImmutableList(),
        onSpotClick = { _, _ -> },
        onTryFindWay = {},
        modifier = Modifier.fillMaxSize()
    )
}