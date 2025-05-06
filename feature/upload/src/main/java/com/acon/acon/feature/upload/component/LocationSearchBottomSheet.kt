package com.acon.acon.feature.upload.component

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acon.acon.core.designsystem.glassmorphism.LocalHazeState
import com.acon.acon.core.designsystem.glassmorphism.defaultHazeEffect
import com.acon.acon.core.designsystem.component.chip.AconChip
import com.acon.acon.core.designsystem.component.dialog.AconOneButtonDialog
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.map.ProceedWithLocation
import com.acon.acon.domain.model.upload.SpotListItem
import com.acon.acon.feature.upload.UploadIntent
import com.acon.acon.feature.upload.UploadViewModel
import com.acon.acon.feature.upload.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LocationSearchBottomSheet(
    viewModel: UploadViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onLocationSelected: (SpotListItem) -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var showVerificationFailDialog by remember { mutableStateOf(false) }
    var processingLocation by remember { mutableStateOf<SpotListItem?>(null) }

    LaunchedEffect(state.locationVerificationResult) {
        when (state.locationVerificationResult) {
            true -> {
                processingLocation?.let { location ->
                    viewModel.onIntent(UploadIntent.SelectLocation(location))
                    onLocationSelected(location)
                    onDismiss()
                }
                processingLocation = null
            }

            false -> {
                showVerificationFailDialog = true
                processingLocation = null
            }

            null -> {}
        }
    }

    if (showVerificationFailDialog) {
        AconOneButtonDialog(
            title = "위치 인식 실패",
            content = "현재 위치와 등록장소가 오차범위 밖에\n있습니다. 좀 더 가까이 이동해보세요.",
            buttonContent = "확인",
            contentImage = R.drawable.and_ic_error_2_104,
            onDismissRequest = {
                showVerificationFailDialog = false
                viewModel.onIntent(UploadIntent.ResetVerification)
            },
            onClickConfirm = {
                showVerificationFailDialog = false
                viewModel.onIntent(UploadIntent.ResetVerification)
            },
            isImageEnabled = true,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }

    LaunchedEffect(searchText) {
        if (searchText.isEmpty()) {
            return@LaunchedEffect
        }
        viewModel.searchFlow.emit(searchText)
    }

    ProceedWithLocation { location ->
        currentLocation = location
        viewModel.onIntent(
            UploadIntent.LoadSuggestions(
                latitude = location.latitude,
                longitude = location.longitude
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(AconTheme.color.Gray9.copy(alpha = 0.5f))
            .defaultHazeEffect(
                hazeState = LocalHazeState.current,
                tintColor = AconTheme.color.Gray8,
                alpha = 0.7f,
                blurRadius = 20.dp
            )
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .align(Alignment.CenterHorizontally)
                .size(width = 36.dp, height = 5.dp)
                .background(
                    color = AconTheme.color.Gray5,
                    shape = RoundedCornerShape(2.dp)
                )
        )

        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .align(Alignment.CenterHorizontally)
                .size(width = 36.dp, height = 5.dp)
        )

        Spacer(modifier = Modifier.padding(top = 12.dp))
        Text(
            text = "장소등록",
            style = AconTheme.typography.head8_16_sb,
            color = AconTheme.color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )

        Spacer(modifier = Modifier.padding(top = 30.dp))
        AconSearchTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = "가게명을 입력해주세요",
            modifier = Modifier.padding(horizontal = 20.dp),
            focusRequester = focusRequester,
            onClick = {
                focusRequester.requestFocus()
                keyboardController?.show()
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
                .padding(horizontal = 20.dp)
        ) {
            when {
                searchText.isEmpty() -> {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.suggestions.forEach { suggestion ->
                            AconChip(
                                title = suggestion.spotName,
                                isSelected = false,
                                onClick = {
                                    val spotItem =
                                        SpotListItem(
                                            spotId = suggestion.spotId,
                                            name = suggestion.spotName,
                                            address = "",
                                            spotType = ""
                                        )

                                    currentLocation?.let { location ->
                                        processingLocation = spotItem
                                        viewModel.onIntent(
                                            UploadIntent.VerifyLocation(
                                                spotId = suggestion.spotId,
                                                latitude = location.latitude,
                                                longitude = location.longitude
                                            )
                                        )
                                    } ?: run {
                                        showVerificationFailDialog = true
                                    }
                                }
                            )
                        }
                    }
                }

                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AconTheme.color.White)
                    }
                }

                !state.isLoading && state.searchResults.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_no_location),
                                contentDescription = null,
                            )

                            Spacer(modifier = Modifier.padding(top = 16.dp))
                            Text(
                                text = "앗! 일치하는 장소가 없어요.",
                                style = AconTheme.typography.body2_14_reg,
                                color = AconTheme.color.Gray4
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn {
                        items(state.searchResults.size) { index ->
                            LocationItem(
                                locationItem = state.searchResults[index],
                                onClick = {
                                    currentLocation?.let { location ->
                                        val selectedItem = state.searchResults[index]
                                        processingLocation = selectedItem
                                        viewModel.onIntent(UploadIntent.ResetVerification)
                                        viewModel.onIntent(
                                            UploadIntent.VerifyLocation(
                                                spotId = selectedItem.spotId,
                                                latitude = location.latitude,
                                                longitude = location.longitude
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
