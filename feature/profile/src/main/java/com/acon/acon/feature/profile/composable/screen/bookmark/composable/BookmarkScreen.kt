package com.acon.acon.feature.profile.composable.screen.bookmark.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.zIndex
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.composable.screen.bookmark.BookmarkUiState
import com.acon.acon.feature.profile.composable.screen.mockSpotList
import com.acon.acon.feature.profile.composable.screen.profile.composable.BookmarkItem
import com.acon.acon.feature.profile.composable.screen.profile.composable.BookmarkSkeletonItem
import com.acon.feature.common.compose.getScreenHeight
import dev.chrisbanes.haze.hazeSource

@Composable
fun BookmarkScreen(
    state: BookmarkUiState,
    modifier: Modifier = Modifier,
    onSpotClick: (Long) -> Unit = {},
    onNavigateToBack: () -> Unit = {}
) {
    val screenHeightDp = getScreenHeight()
    val skeletonHeight = (screenHeightDp * 0.07f)

    when (state) {
        is BookmarkUiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .defaultHazeEffect(
                        hazeState = LocalHazeState.current,
                        tintColor = Color(0xFF1C1C20),
                        blurRadius = 20.dp,
                    )
                    .statusBarsPadding()
            ) {
                AconTopBar(
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
                        .padding(vertical = 14.dp)
                        .zIndex(1f)
                )

                Column(
                    modifier = Modifier
                        .padding(top = 72.dp)
                        .padding(horizontal = 16.dp)
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .hazeSource(LocalHazeState.current)
                ) {
                    mockSpotList.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { spot ->
                                BookmarkSkeletonItem(
                                    skeletonHeight = skeletonHeight,
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(160f / 231f)
                                )
                            }
                            if (rowItems.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        is BookmarkUiState.Success -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .defaultHazeEffect(
                        hazeState = LocalHazeState.current,
                        tintColor = Color(0xFF1C1C20),
                        blurRadius = 20.dp,
                    )
                    .statusBarsPadding()
            ) {
                AconTopBar(
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
                        .padding(vertical = 14.dp)
                        .zIndex(1f)
                )

                Column(
                    modifier = Modifier
                        .padding(top = 72.dp)
                        .padding(horizontal = 16.dp)
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .hazeSource(LocalHazeState.current)
                ) {
                    state.savedSpots?.chunked(2)?.fastForEach { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { spot ->
                                BookmarkItem(
                                    spot = spot,
                                    onClickSpotItem = { onSpotClick(spot.id) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(160f / 231f)
                                )
                            }
                            if (rowItems.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        is BookmarkUiState.LoadFailed -> {}
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