package com.acon.acon.feature.profile.composable.screen.profile.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottombar.AconBottomBar
import com.acon.acon.core.designsystem.component.bottombar.BottomNavType
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.profile.ProfileInfo
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.profile.composable.screen.mockSpotList
import com.acon.acon.feature.profile.composable.screen.profile.ProfileUiState
import com.acon.feature.common.compose.LocalRequestSignIn
import com.acon.feature.common.compose.LocalUserType
import com.acon.feature.common.compose.getScreenHeight
import dev.chrisbanes.haze.hazeSource

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileScreen(
    state: ProfileUiState,
    modifier: Modifier = Modifier,
    onSpotDetail: (Long) -> Unit = {},
    onBookmark: () -> Unit = {},
    onSettings: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onNavigateToSpotListScreen: () -> Unit = {},
    onNavigateToUploadScreen: () -> Unit = {},
) {
    val screenHeightDp = getScreenHeight()
    val profileImageHeight = (screenHeightDp * (60f / 740f))
    val admobHeight = (screenHeightDp * (165f / 740f))
    val savedStoreHeight = (screenHeightDp * (200f / 740f))

    val userType = LocalUserType.current
    val onSignInRequired = LocalRequestSignIn.current

    Column(modifier) {
        when (state) {
            is ProfileUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .hazeSource(LocalHazeState.current)
                ) {
                    AconTopBar(
                        content = {
                            Text(
                                text = stringResource(R.string.profile_topbar),
                                style = AconTheme.typography.Title4,
                                fontWeight = FontWeight.SemiBold,
                                color = AconTheme.color.White
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { onSettings() }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_setting),
                                    contentDescription = stringResource(R.string.content_description_settings),
                                    tint = AconTheme.color.White
                                )
                            }
                        },
                        modifier = Modifier.padding(vertical = 14.dp)
                    )

                    Column(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Row {
                            if (state.profileInfo.image.isEmpty()) {
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_default_profile),
                                    contentDescription = stringResource(R.string.content_description_default_profile_image),
                                    modifier = Modifier
                                        .size(profileImageHeight)
                                        .clip(CircleShape)
                                )
                            } else {
                                AsyncImage(
                                    model = state.profileInfo.image,
                                    contentDescription = stringResource(R.string.content_description_profile_image),
                                    modifier = Modifier
                                        .size(profileImageHeight)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                    error = painterResource(R.drawable.ic_default_profile)
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .padding(start = 16.dp)
                            ) {
                                Text(
                                    text = state.profileInfo.nickname,
                                    style = AconTheme.typography.Headline4,
                                    color = AconTheme.color.White
                                )

                                Spacer(Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.edit_profile),
                                        style = AconTheme.typography.Body1,
                                        color = AconTheme.color.Gray500,
                                    )

                                    Image(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
                                        contentDescription = stringResource(R.string.content_description_edit_profile),
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .noRippleClickable { onEditProfile() }
                                    )
                                }
                            }
                        }

                        // TODO - saveSpot = isEmpty -> 저장한 장소가 없어요.

                        Spacer(Modifier.height(42.dp))
                        if (state.profileInfo != ProfileInfo.Empty) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.saved_store),
                                    color = AconTheme.color.White,
                                    style = AconTheme.typography.Title4,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(Modifier.weight(1f))
                                if (state.profileInfo.savedSpots.isNotEmpty()) {
                                    Text(
                                        text = stringResource(R.string.show_saved_all_store),
                                        color = AconTheme.color.Action,
                                        style = AconTheme.typography.Body1,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .padding(vertical = 2.dp)
                                            .padding(end = 8.dp)
                                            .noRippleClickable { onBookmark() }
                                    )
                                }
                            }

                            Spacer(Modifier.height(8.dp))
                            if (state.profileInfo.savedSpots.isNotEmpty()) {
                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(savedStoreHeight),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(
                                        items = state.profileInfo.savedSpots,
                                        key = { it.spotId }
                                    ) { spot ->
                                        BookmarkItem(
                                            spot = spot,
                                            onClickSpotItem = { onSpotDetail(spot.spotId) },
                                            modifier = Modifier.aspectRatio(150f / 217f)
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = stringResource(R.string.no_saved_spot),
                                    style = AconTheme.typography.Body1,
                                    fontWeight = FontWeight.W400,
                                    color = AconTheme.color.Gray500,
                                )
                            }
                        }
                        Spacer(Modifier.height(if (state.profileInfo.savedSpots.isEmpty()) 40.dp else 20.dp))
//                        ProfileNativeAd(
//                            screenHeight = admobHeight,
//                            modifier = Modifier.padding(bottom = 23.dp)
//                        )
                    }
                }
            }

            is ProfileUiState.Guest -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .hazeSource(LocalHazeState.current)
                ) {
                    AconTopBar(
                        content = {
                            Text(
                                text = stringResource(R.string.profile_topbar),
                                style = AconTheme.typography.Title4,
                                fontWeight = FontWeight.SemiBold,
                                color = AconTheme.color.White
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { onSettings() }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_setting),
                                    contentDescription = stringResource(R.string.content_description_settings),
                                    tint = AconTheme.color.White
                                )
                            }
                        },
                        modifier = Modifier.padding(vertical = 14.dp)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 40.dp)
                            .padding(horizontal = 16.dp)

                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_default_profile),
                                contentDescription = stringResource(R.string.content_description_default_profile_image),
                                modifier = Modifier
                                    .size(profileImageHeight)
                                    .clip(CircleShape)
                            )

                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .noRippleClickable { onSignInRequired(null) }
                            ) {
                                Text(
                                    text = stringResource(R.string.you_need_sign_in),
                                    style = AconTheme.typography.Headline4,
                                    color = AconTheme.color.White,
                                )

                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_24),
                                    contentDescription = stringResource(R.string.content_description_go_sign_in),
                                    modifier = Modifier.padding(start = 4.dp),
                                    tint = AconTheme.color.Gray50
                                )
                            }
                        }

//                        ProfileNativeAd(
//                            screenHeight = admobHeight,
//                            modifier = Modifier.padding(top = 20.dp)
//                        )
                    }
                }
            }
        }
        AconBottomBar(
            selectedItem = BottomNavType.PROFILE,
            onItemClick = { bottomType ->
                when (bottomType) {
                    BottomNavType.SPOT -> {
                        onNavigateToSpotListScreen()
                    }

                    BottomNavType.UPLOAD -> {
                        if (userType == UserType.GUEST) {
                            onSignInRequired(null)
                        } else {
                            onNavigateToUploadScreen()
                        }
                    }

                    BottomNavType.PROFILE -> Unit
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultHazeEffect(
                    hazeState = LocalHazeState.current,
                    tintColor = AconTheme.color.GlassGray900
                )
                .navigationBarsPadding()
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    AconTheme {
        ProfileScreen(
            state = ProfileUiState.Guest
        )
    }
}