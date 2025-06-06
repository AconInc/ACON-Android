package com.acon.acon.feature.profile.composable.screen.profile.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.LoginBottomSheet
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.composable.amplitude.profileAmplitude
import com.acon.acon.feature.profile.composable.screen.mockSpotList
import com.acon.acon.feature.profile.composable.screen.profile.ProfileUiState

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileScreen(
    state: ProfileUiState,
    modifier: Modifier = Modifier,
    onBookmark: () -> Unit = {},
    onSettings: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onGoogleSignIn: () -> Unit = {},
    onBottomSheetShowStateChange: (Boolean) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val boxHeight = (screenHeightDp * (60f / 740f))
    val savedStoreHeight = (screenHeightDp * (217f / 740f))

    when (state) {
        is ProfileUiState.Success -> {
            Column(
                modifier = modifier
                    .background(AconTheme.color.Gray900)
                    .padding(horizontal = 16.dp)

            ) {
                Spacer(Modifier.height(40.dp))
                AconTopBar(
                    modifier = Modifier.padding(vertical = 14.dp),
                    paddingValues = PaddingValues(0.dp),
                    content = {
                        Text(
                            text = stringResource(R.string.profile_topbar),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = onSettings) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_setting),
                                contentDescription = stringResource(R.string.content_description_settings),
                                tint = AconTheme.color.White
                            )
                        }
                    },
                )

                Row(
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    if (state.profileImage.isEmpty()) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_default_profile),
                            contentDescription = stringResource(R.string.content_description_default_profile_image),
                            modifier = Modifier
                                .size(boxHeight.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        AsyncImage(
                            model = state.profileImage,
                            contentDescription = stringResource(R.string.content_description_profile_image),
                            modifier = Modifier
                                .size(boxHeight.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = state.nickname,
                            style = AconTheme.typography.Headline4,
                            color = AconTheme.color.White
                        )

                        Spacer(Modifier.height(4.dp))
                        Row {
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

                Spacer(Modifier.height(42.dp))
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

                Spacer(Modifier.height(8.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(savedStoreHeight.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = mockSpotList,
                        key = { it.id }
                    ) { spot ->
                        BookmarkItem(
                            spot = spot,
                            onClickSpotItem = {}, // TODO - 장소 상세로 이동
                            modifier = Modifier.aspectRatio(150f/217f)
                        )
                    }
                }

                ProfileAdmobBanner(
                    modifier = Modifier.padding(top = 20.dp, bottom = 48.dp)
                )
            }
        }

        is ProfileUiState.Loading -> {}
        is ProfileUiState.LoadFailed -> {}

        is ProfileUiState.Guest -> {
            if (state.showLoginBottomSheet) {
                LoginBottomSheet(
                    onDismissRequest = { onBottomSheetShowStateChange(false) },
                    onGoogleSignIn = {
                        onGoogleSignIn()
                        profileAmplitude()
                    }
                )
            }

            Column(
                modifier = modifier
                    .background(AconTheme.color.Gray900)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(40.dp))
                AconTopBar(
                    modifier = Modifier.padding(vertical = 14.dp),
                    paddingValues = PaddingValues(0.dp),
                    content = {
                        Text(
                            text = stringResource(R.string.profile_topbar),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = onSettings) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_setting),
                                contentDescription = stringResource(R.string.content_description_settings),
                                tint = AconTheme.color.White
                            )
                        }
                    }
                )

                Row(
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_default_profile),
                        contentDescription = stringResource(R.string.content_description_default_profile_image),
                        modifier = Modifier
                            .size(boxHeight.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = stringResource(R.string.you_need_login),
                        style = AconTheme.typography.Headline4,
                        color = AconTheme.color.White,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .padding(vertical = 16.dp)
                            .noRippleClickable { onBottomSheetShowStateChange(true) }
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_24),
                        contentDescription = stringResource(R.string.content_description_edit_profile),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .padding(vertical = 16.dp)
                            .noRippleClickable { onBottomSheetShowStateChange(true) },
                        tint = AconTheme.color.White
                    )
                }

                Spacer(Modifier.weight(1f))
                ProfileAdmobBanner(
                    modifier = Modifier.padding(bottom = 48.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    AconTheme {
        ProfileScreen(
            state = ProfileUiState.Guest(),
        )
    }
}
