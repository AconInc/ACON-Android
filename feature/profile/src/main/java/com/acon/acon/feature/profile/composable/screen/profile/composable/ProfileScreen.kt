package com.acon.acon.feature.profile.composable.screen.profile.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.LoginBottomSheet
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.composable.amplitude.profileAmplitude
import com.acon.acon.feature.profile.composable.screen.profile.ProfileUiState
import dev.chrisbanes.haze.hazeSource

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    modifier: Modifier = Modifier,
    onSettings: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onGoogleSignIn: () -> Unit = {},
    onBottomSheetShowStateChange: (Boolean) -> Unit = {}
) {
    when (state) {
        is ProfileUiState.Success -> {
            Column(
                modifier = modifier
                    .background(AconTheme.color.Gray900)
                    .padding(horizontal = 16.dp)
                    .hazeSource(LocalHazeState.current)
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
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                ) {
                    if (state.profileImage.isEmpty()) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_default_profile),
                            contentDescription = stringResource(R.string.content_description_default_profile_image),
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        AsyncImage(
                            model = state.profileImage,
                            contentDescription = stringResource(R.string.content_description_profile_image),
                            modifier = Modifier
                                .size(60.dp)
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
                    .hazeSource(LocalHazeState.current)
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
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_default_profile),
                        contentDescription = stringResource(R.string.content_description_default_profile_image),
                        modifier = Modifier
                            .size(60.dp)
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
