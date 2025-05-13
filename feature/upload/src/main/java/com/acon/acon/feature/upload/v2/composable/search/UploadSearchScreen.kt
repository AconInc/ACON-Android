package com.acon.acon.feature.upload.v2.composable.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.zIndex
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.chip.AconChip
import com.acon.acon.core.designsystem.component.textfield.v2.AconSearchTextField
import com.acon.acon.core.designsystem.glassmorphism.LocalHazeState
import com.acon.acon.core.designsystem.glassmorphism.defaultHazeEffect
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.upload.v2.SearchedSpot
import com.acon.acon.feature.upload.mock.uploadSearchUiStateMock
import com.acon.acon.feature.upload.v2.UploadSearchUiState
import com.acon.feature.common.type.getNameResId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun UploadSearchScreen(
    state: UploadSearchUiState,
    onSearchQueryChanged: (String) -> Unit,
    onBackAction: () -> Unit,
    onNextAction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val isRightActionEnabled by remember(state) {
        derivedStateOf {
            when (state) {
                is UploadSearchUiState.Success -> state.query.isNotEmpty()
                else -> false
            }
        }
    }

    Column(
        modifier = modifier,
    ) {
        UploadTopAppBar(
            isRightActionEnabled = isRightActionEnabled,
            onLeftAction = onBackAction,
            onRightAction = onNextAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )

        when(state) {
            is UploadSearchUiState.Success -> {
                Column(
                    Modifier.padding(horizontal = 16.dp)
                ) {
                    AconSearchTextField(
                        value = state.query,
                        onValueChange = onSearchQueryChanged,
                        placeholder = stringResource(R.string.search_spot_placeholder),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box {
                        // TODO FlowRow
                        FlowRow(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            state.recommends.fastForEach {
                                AconChip(
                                    title = it,
                                    onClick = { onSearchQueryChanged(it) },
                                    isSelected = false
                                )
                            }
                        }
                        if (state.query.isNotEmpty()) {
                            SearchedSpots(
                                searchedSpots = state.searchedSpots.toImmutableList(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .height(300.dp)
                                    .background(
                                        color = AconTheme.color.GlassWhiteDefault,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .defaultHazeEffect(
                                        hazeState = LocalHazeState.current,
                                        tintColor = AconTheme.color.GlassWhiteDefault,
                                    )
                                    .zIndex(2f)
                            )
                        }
                    }
                }
            }
            UploadSearchUiState.LoadFailed -> {
                // TODO
            }
        }
    }
}

@Composable
private fun SearchedSpots(
    modifier: Modifier = Modifier,
    searchedSpots: ImmutableList<SearchedSpot>,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (searchedSpots.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.no_search_result),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 20.dp)
                )

                Text(
                    text = stringResource(R.string.request_new_spot_description1),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 60.dp)
                )
                Text(
                    text = stringResource(R.string.request_new_spot_description2),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.Gray500,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = stringResource(R.string.go_to_register_spot),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.Action,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }

        items(
            items = searchedSpots,
            key = { it.spotId },
        ) {
            SearchedSpotItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                searchedSpot = it,
            )
        }
    }
}

@Composable
private fun SearchedSpotItem(
    searchedSpot: SearchedSpot,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = searchedSpot.name,
                style = AconTheme.typography.Title4,
                color = AconTheme.color.White,
            )
            Text(
                text = searchedSpot.address,
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Gray500,
            )
        }
        Text(
            text = stringResource(searchedSpot.spotType.getNameResId()),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray500,
        )
    }
}

@Preview
@Composable
private fun UploadSearchScreenPreview() {
    UploadSearchScreen(
        state = uploadSearchUiStateMock,
        onSearchQueryChanged = {},
        onBackAction = {},
        onNextAction = {},
        modifier = Modifier
    )
}