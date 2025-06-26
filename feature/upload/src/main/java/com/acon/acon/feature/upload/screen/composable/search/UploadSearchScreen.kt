package com.acon.acon.feature.upload.screen.composable.search

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.acon.acon.core.common.UrlConstants
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.chip.AconChip
import com.acon.acon.core.designsystem.component.dialog.v2.AconDefaultDialog
import com.acon.acon.core.designsystem.component.textfield.v2.AconSearchTextField
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.effect.rememberHazeState
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.toast.showToast
import com.acon.core.model.upload.UploadSpotSuggestion
import com.acon.core.model.upload.SearchedSpot
import com.acon.acon.feature.upload.mock.uploadSearchUiStateMock
import com.acon.acon.feature.upload.screen.UploadSearchUiState
import com.acon.core.analytics.amplitude.AconAmplitude
import com.acon.core.analytics.constants.EventNames
import com.acon.core.analytics.constants.PropertyKeys
import com.acon.core.ui.ext.getNameResId
import dev.chrisbanes.haze.hazeSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun UploadSearchScreen(
    state: UploadSearchUiState,
    onSearchQueryChanged: (String, isSelection: Boolean) -> Unit,
    onSearchedSpotClick: (SearchedSpot, onSuccess: () -> Unit) -> Unit,
    onSuggestionSpotClick: (UploadSpotSuggestion, onSuccess: () -> Unit) -> Unit,
    onVerifyLocationDialogAction: () -> Unit,
    onBackAction: () -> Unit,
    onNextAction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val hazeState = rememberHazeState()

    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var isSelection by remember { mutableStateOf(false) }

    val isNextActionEnabled by remember(state) {
        derivedStateOf {
            when (state) {
                is UploadSearchUiState.Success -> state.selectedSpot != null
                else -> false
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { query }.collect {
            onSearchQueryChanged(it.text, isSelection)
        }
    }

    Column(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                keyboardController?.hide()
                focusManager.clearFocus()
            })
        },
    ) {
        UploadTopAppBar(
            isRightActionEnabled = isNextActionEnabled,
            onLeftAction = onBackAction,
            onRightAction = onNextAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )

        when(state) {
            is UploadSearchUiState.Success -> {
                if (state.showNotAvailableLocationDialog || state.showNotInKoreaDialog) {
                    AconDefaultDialog(
                        title = stringResource(R.string.failed_verify_location),
                        action = stringResource(R.string.ok),
                        onAction = onVerifyLocationDialogAction,
                        onDismissRequest = onVerifyLocationDialogAction
                    ) {
                        Text(
                            text = stringResource(
                                if (state.showNotAvailableLocationDialog)
                                    R.string.failed_verify_location_description
                                else
                                    R.string.failed_verify_location_not_in_korea_description
                            ),
                            style = AconTheme.typography.Body1,
                            color = AconTheme.color.Gray200,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 20.dp)
                        )
                    }
                }
                Column(
                    Modifier.padding(horizontal = 16.dp)
                ) {
                    AconSearchTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            isSelection = false
                        },
                        placeholder = stringResource(R.string.search_spot_placeholder),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            state.uploadSpotSuggestions.fastForEach {
                                AconChip(
                                    title = it.name,
                                    onClick = {
                                        onSuggestionSpotClick(it) {
                                            isSelection = true
                                            query = TextFieldValue(text = it.name, selection = TextRange(it.name.length))
                                        }
                                    },
                                    isSelected = false,
                                    modifier = Modifier.hazeSource(state = hazeState)
                                )
                            }
                        }

                        if (state.showSearchedSpots) {
                            SearchedSpots(
                                searchedSpots = state.searchedSpots.toImmutableList(),
                                onItemClick = {
                                    onSearchedSpotClick(it) {
                                        isSelection = true
                                        query = TextFieldValue(text = it.name, selection = TextRange(it.name.length))
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .then(
                                        if (state.uploadSpotSuggestions.isEmpty())
                                            Modifier.background(AconTheme.color.GlassWhiteLight)
                                        else Modifier.defaultHazeEffect(
                                            hazeState = hazeState,
                                            tintColor = AconTheme.color.Gray800,
                                            blurRadius = 20.dp,
                                            alpha = .4f
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchedSpots(
    searchedSpots: ImmutableList<SearchedSpot>,
    onItemClick: (SearchedSpot) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
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
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .noRippleClickable {
                                AconAmplitude.trackEvent(
                                    eventName = EventNames.UPLOAD,
                                    property = PropertyKeys.CLICK_REGISTER_FORM to true
                                )
                                try {
                                    uriHandler.openUri(UrlConstants.REQUEST_NEW_SPOT_UPLOAD)
                                } catch (e: Exception) {
                                    context.showToast("웹사이트 접속에 실패했어요")
                                }
                            }
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
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .noRippleClickable {
                            onItemClick(it)
                        },
                    searchedSpot = it,
                )
            }
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
        onSearchQueryChanged = { _, _ -> },
        onBackAction = {},
        onNextAction = {},
        onSearchedSpotClick = {_, _ ->},
        onVerifyLocationDialogAction = {},
        onSuggestionSpotClick = {_, _ ->},
        modifier = Modifier
    )
}