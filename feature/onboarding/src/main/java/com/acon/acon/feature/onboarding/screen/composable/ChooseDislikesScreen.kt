package com.acon.acon.feature.onboarding.screen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.component.chip.AconChip
import com.acon.acon.core.designsystem.component.dialog.v2.AconTwoActionDialog
import com.acon.acon.core.designsystem.effect.effect.shadowLayerBackground
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.onboarding.screen.ChooseDislikesUiState
import com.acon.core.navigation.LocalNavController
import com.acon.core.type.FoodType
import com.acon.core.ui.ext.getNameResId

@Composable
internal fun ChooseDislikesScreen(
    state: ChooseDislikesUiState,
    onComplete: () -> Unit,
    onNoneChosen: () -> Unit,
    onDislikeFoodChosen: (FoodType) -> Unit,
    modifier: Modifier = Modifier,
    onDismissStopModal: (() -> Unit) -> Unit = {},
) {
    val navController = LocalNavController.current
    when (state) {
        is ChooseDislikesUiState.Success -> {
            if (state.showStopModal) {
                AconTwoActionDialog(
                    title = stringResource(R.string.stop_onboarding),
                    action1 = stringResource(R.string.keep_going),
                    action2 = stringResource(R.string.stop),
                    onAction1 = { onDismissStopModal{} },
                    onAction2 = {
                        onDismissStopModal {
                            navController.navigateUp()
                        }
                    },
                    onDismissRequest = { onDismissStopModal{} }
                )
            }

            val isFoodChipsEnabled by remember(state) {
                mutableStateOf(state.isNoneChosen.not())
            }

            val isCompleteButtonEnabled by remember(state) {
                derivedStateOf {
                    state.selectedDislikes.isNotEmpty() || state.isNoneChosen
                }
            }

            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.request_dislike_food_title),
                    style = AconTheme.typography.Title1,
                    fontWeight = FontWeight.SemiBold,
                    color = AconTheme.color.White,
                )
                Text(
                    text = stringResource(R.string.request_dislike_food_description),
                    style = AconTheme.typography.Title5,
                    fontWeight = FontWeight.W400,
                    color = AconTheme.color.Gray500,
                    modifier = Modifier.padding(top = 10.dp)
                )

                AconChip(
                    title = stringResource(R.string.no_dislike_food),
                    isSelected = state.isNoneChosen,
                    onClick = onNoneChosen,
                    modifier = Modifier.padding(top = 40.dp)
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FoodType.entries.fastForEach { foodType ->
                        AconChip(
                            title = stringResource(foodType.getNameResId()),
                            isSelected = state.selectedDislikes.contains(foodType),
                            onClick = {
                                onDislikeFoodChosen(foodType)
                            },
                            enabled = isFoodChipsEnabled
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = if (state.isNoneChosen) stringResource(R.string.user_choose_none) else if (state.selectedDislikes.isNotEmpty()) stringResource(
                        R.string.user_choose_dislike_food
                    ) else "",
                    style = AconTheme.typography.Body1,
                    fontWeight = FontWeight.W400,
                    color = AconTheme.color.Gray200,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)) {
                    AconFilledTextButton(
                        text = stringResource(R.string.start),
                        onClick = onComplete,
                        enabled = isCompleteButtonEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(150.dp)
                            .align(Alignment.Center)
                            .then(
                                if (isCompleteButtonEnabled) Modifier.shadowLayerBackground(
                                    shadowColor = AconTheme.color.PrimaryDefault,
                                    shadowAlpha = .5f,
                                    shadowRadius = 500f
                                ) else Modifier
                            )
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ChooseDislikesScreenPreview() {
    ChooseDislikesScreen(
        state = ChooseDislikesUiState.Success(
            selectedDislikes = emptySet(),
            isNoneChosen = false
        ),
        onComplete = {},
        onNoneChosen = {},
        onDislikeFoodChosen = {},
        modifier = Modifier
            .padding(top = 86.dp, bottom = 16.dp)
            .padding(horizontal = 16.dp)
    )
}