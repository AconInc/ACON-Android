package com.acon.acon.feature.profile.composable.screen.bookmark.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.effect.imageGradientLayer
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.composable.screen.bookmark.BookmarkUiState
import com.acon.acon.feature.profile.composable.screen.mockSpotList
import com.acon.acon.feature.profile.composable.screen.profile.composable.BookmarkItem
import com.acon.acon.feature.profile.composable.screen.profile.composable.BookmarkSkeletonItem
import dev.chrisbanes.haze.hazeSource

@Composable
fun BookmarkScreen(
    state: BookmarkUiState,
    modifier: Modifier = Modifier,
    onSpotClick: (Long) -> Unit = {},
    onNavigateToBack: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val skeletonHeight = (screenHeightDp * 0.07f).dp

    when(state) {
        BookmarkUiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Gray900)
                    .statusBarsPadding()
            ) {
                AconTopBar(
                    paddingValues = PaddingValues(0.dp),
                    leadingIcon = {
                        IconButton(
                            onClick = onNavigateToBack
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_topbar_arrow_left),
                                contentDescription = stringResource(R.string.back),
                                tint = AconTheme.color.White
                            )
                        }
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.saved_store),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    modifier = Modifier
                        .imageGradientLayer()
                        .defaultHazeEffect(
                            hazeState = LocalHazeState.current,
                            tintColor = AconTheme.color.Gray900,
                            blurRadius = 20.dp,
                        )
                        .zIndex(1f)
                )

                Column(
                    modifier = Modifier
                        .padding(top = 72.dp)
                        .padding(horizontal = 16.dp)
                        .navigationBarsPadding()
                        .hazeSource(LocalHazeState.current)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                           mockSpotList
                        ) { spot ->
                            BookmarkSkeletonItem(
                                skeletonHeight = skeletonHeight,
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(160f / 231f)
                            )
                        }
                    }
                }
            }
        }
        BookmarkUiState.Success -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Gray900)
                    .statusBarsPadding()
            ) {
                AconTopBar(
                    paddingValues = PaddingValues(0.dp),
                    leadingIcon = {
                        IconButton(
                            onClick = onNavigateToBack
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_topbar_arrow_left),
                                contentDescription = stringResource(R.string.back),
                                tint = AconTheme.color.White
                            )
                        }
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.saved_store),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    modifier = Modifier
                        .imageGradientLayer()
                        .defaultHazeEffect(
                            hazeState = LocalHazeState.current,
                            tintColor = AconTheme.color.Gray900,
                            blurRadius = 20.dp,
                        )
                        .zIndex(1f)
                )

                Column(
                    modifier = Modifier
                        .padding(top = 72.dp)
                        .padding(horizontal = 16.dp)
                        .navigationBarsPadding()
                        .hazeSource(LocalHazeState.current)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            mockSpotList
                        ) { spot ->
                            BookmarkItem(
                                spot = spot,
                                onClickSpotItem = { onSpotClick(1) },
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(160f / 231f)
                            )
                        }
                    }
                }
            }
        }
        BookmarkUiState.LoadFailed -> {}
    }
}

@Preview
@Composable
fun BookmarkScreenPreview() {
    AconTheme {
        BookmarkScreen(
            state = BookmarkUiState.Loading
        )
    }
}