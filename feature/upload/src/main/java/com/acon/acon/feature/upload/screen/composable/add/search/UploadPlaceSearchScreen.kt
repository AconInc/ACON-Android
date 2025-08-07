package com.acon.acon.feature.upload.screen.composable.add.search

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.animation.slideUpAnimation
import com.acon.acon.core.designsystem.component.dialog.v2.AconTwoActionDialog
import com.acon.acon.core.designsystem.component.textfield.v2.AconSearchTextField
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.model.model.upload.SearchedSpotByMap
import com.acon.acon.core.ui.compose.getScreenWidth
import com.acon.acon.feature.upload.screen.UploadPlaceUiState
import dev.chrisbanes.haze.hazeSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun UploadPlaceSearchScreen(
    state: UploadPlaceUiState,
    onBackAction: () -> Unit,
    onClickReportPlace: () -> Unit,
    onHideSearchedPlaceList: () -> Unit,
    onSearchedSpotClick: (SearchedSpotByMap, onUpdateTextField: () -> Unit) -> Unit,
    onSearchQueryOrSelectionChanged: (String, Boolean) -> Unit,
    onAnimationEnded: (String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val screenWidthDp = getScreenWidth()
    val dialogWidth = (screenWidthDp * (260f / 360f))

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(state.selectedSpotByMap?.title ?: "")) }
    var isSelection by remember { mutableStateOf(false) }

    var offsetY by remember { mutableIntStateOf(0) }
    var showOffset by remember { mutableStateOf(false) }
    val offsetYAnimated by animateIntAsState(
        targetValue = if (showOffset) -offsetY else 0,
        animationSpec = tween(durationMillis = 300)
    )

    val hasAnimated = state.hasAnimated["1"] ?: false

    LaunchedEffect(Unit) {
        snapshotFlow { query }.collect {
            onSearchQueryOrSelectionChanged(it.text, isSelection)
        }
    }

    LaunchedEffect(lifecycleOwner, Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            if (state.selectedSpotByMap?.title?.isNotEmpty() == true) {
                onHideSearchedPlaceList()
            }
        }
    }

    if(state.showUploadPlaceLimitDialog) {
        AconTwoActionDialog(
            title = stringResource(R.string.upload_place_limit_dialog_title),
            action1 = stringResource(R.string.end),
            action2 = stringResource(R.string.report),
            onDismissRequest = {},
            onAction1 = {
                onBackAction()
            },
            onAction2 = {
                onClickReportPlace()
            },
            modifier = Modifier.width(dialogWidth)
        ) {
            Text(
                text = stringResource(R.string.upload_place_limit_dialog_content),
                color = AconTheme.color.Gray200,
                style = AconTheme.typography.Body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 22.dp)
            )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray900)
            .padding(horizontal = 16.dp)
            .offset {
                IntOffset(x = 0, y = offsetYAnimated)
            }
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    showOffset = false
                })
            }
    ) {
        Column(
            modifier = Modifier
                .hazeSource(LocalHazeState.current)
                .onGloballyPositioned { coordinates ->
                    offsetY = coordinates.size.height
                }
        ) {
            Text(
                text = stringResource(R.string.required_field),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Danger,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .then(if (!hasAnimated) Modifier.slideUpAnimation(order = 1) else Modifier)
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.upload_place_search_title),
                style = AconTheme.typography.Headline3,
                color = AconTheme.color.White,
                modifier = Modifier
                    .then(if (!hasAnimated) Modifier.slideUpAnimation(order = 2) else Modifier)
            )

            Spacer(Modifier.height(32.dp))
        }

        Column {
            AconSearchTextField(
                value = query,
                onValueChange = {
                    query = it
                    isSelection = false
                },
                placeholder = stringResource(R.string.upload_place_search_placeholder),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        showOffset = false
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (!hasAnimated) Modifier.slideUpAnimation(
                            order = 4,
                            onAnimationEnded = { onAnimationEnded("1") }
                        ) else Modifier
                    )
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            showOffset = true
                        }
                    }
            )
        }
        Box {
            if (state.showSearchedSpotsByMap) {
                SearchedSpots(
                    searchedSpotsByMap = state.searchedSpotsByMap.toImmutableList(),
                    onItemClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onSearchedSpotClick(it) {
                            isSelection = true
                            query = TextFieldValue(
                                text = it.title,
                                selection = TextRange(it.title.length)
                            )
                            showOffset = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .height(300.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AconTheme.color.GlassWhiteLight)
                )
            }
        }
    }
}

@Composable
private fun SearchedSpots(
    searchedSpotsByMap: ImmutableList<SearchedSpotByMap>,
    onItemClick: (SearchedSpotByMap) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (searchedSpotsByMap.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.upload_place_no_search_result),
                        style = AconTheme.typography.Body1,
                        color = AconTheme.color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 20.dp)
                    )

                    Text(
                        text = stringResource(R.string.upload_place_no_search_try_again),
                        style = AconTheme.typography.Body1,
                        color = AconTheme.color.White,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 40.dp)
                    )
                }
            }

            items(
                items = searchedSpotsByMap,
                key = { it.id },
            ) {
                SearchedSpotItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .noRippleClickable {
                            onItemClick(it)
                        },
                    searchedSpotByMap = it
                )
            }
        }
    }
}

@Composable
private fun SearchedSpotItem(
    searchedSpotByMap: SearchedSpotByMap,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = searchedSpotByMap.title,
                style = AconTheme.typography.Title4,
                color = AconTheme.color.White
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = searchedSpotByMap.roadAddress.takeIf { it.isNotEmpty() } ?: searchedSpotByMap.address,
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Gray500
            )
        }

        Text(
            text = searchedSpotByMap.category,
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray500
        )
    }
}

@Preview
@Composable
private fun UploadPlaceSearchScreenPreview() {
    AconTheme {
        UploadPlaceSearchScreen(
            state = UploadPlaceUiState(),
            onBackAction = { },
            onClickReportPlace = {},
            onHideSearchedPlaceList = {},
            onSearchedSpotClick = { _, _ -> },
            onSearchQueryOrSelectionChanged = { _, _ -> },
            onAnimationEnded = {}
        )
    }
}