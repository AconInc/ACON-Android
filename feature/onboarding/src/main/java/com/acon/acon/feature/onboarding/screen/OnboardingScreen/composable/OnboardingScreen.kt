package com.acon.acon.feature.onboarding.screen.OnboardingScreen.composable

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.button.AconFilledLargeButton
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.onboarding.component.FoodGrid
import com.acon.acon.feature.onboarding.component.FreqPlaceSelectGrid
import com.acon.acon.feature.onboarding.component.OnboardingTopBar
import com.acon.acon.feature.onboarding.component.PreferFoodTypeSelectGrid
import com.acon.acon.feature.onboarding.component.PreferPlaceTypeSelectGrid
import com.acon.acon.feature.onboarding.screen.OnboardingScreen.OnboardingPageState
import com.acon.acon.feature.onboarding.screen.OnboardingScreen.OnboardingScreenState

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    screenState: OnboardingScreenState,
    onCardClicked: (String) -> Unit,
    onBackClicked: () -> Unit = {},
    onSkipClicked: () -> Unit = {},
    navigateToNextPage: () -> Unit,
){
//    val context = LocalContext.current

    BackHandler(enabled = true) {
        onBackClicked()
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = AconTheme.color.Gray9)
    ){
        OnboardingTopBar(
            totalPages = screenState.totalPages,
            currentPage = screenState.currentPage,
            onLeadingIconClicked = {
                if (screenState.currentPage == 1){
                    onSkipClicked()
                } else {
                    onBackClicked()
                }
            },
            onTrailingIconClicked = {
                onSkipClicked()
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 15.dp, vertical = 30.dp)
                .verticalScroll(scrollState),
        ){

            Text(
                text = when (val currentState = screenState.currentState) {
                    is OnboardingPageState.Page1State -> currentState.titleNum.toString()
                    is OnboardingPageState.Page2State -> currentState.titleNum.toString()
                    is OnboardingPageState.Page3State -> currentState.titleNum.toString()
                    is OnboardingPageState.Page4State -> currentState.titleNum.toString()
                    is OnboardingPageState.Page5State -> currentState.titleNum.toString()
                },
                color = AconTheme.color.Gray5,
                style = AconTheme.typography.head4_24_sb,
                modifier = modifier.padding(vertical = 7.dp)
            )
            Text(
                text = stringResource(
                    id = when (val currentState = screenState.currentState) {
                        is OnboardingPageState.Page1State -> currentState.pageDescription
                        is OnboardingPageState.Page2State -> currentState.pageDescription
                        is OnboardingPageState.Page3State -> currentState.pageDescription
                        is OnboardingPageState.Page4State -> currentState.pageDescription
                        is OnboardingPageState.Page5State -> currentState.pageDescription
                    }
                ),
                color = Color.White,
                style = AconTheme.typography.head4_24_sb,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier.fillMaxWidth().height(350.dp)
            ){
                when (val currentState = screenState.currentState) {
                    is OnboardingPageState.Page1State -> {
                        FoodGrid(
                            modifier = Modifier.fillMaxSize(),
                            columnSize = currentState.columnSize,
                            foodItems = currentState.foodItems.toTypedArray(),
                            onCardClicked = onCardClicked,
                            isNothingClicked = currentState.isNothingClicked,
                            selectedCard = currentState.selectedCard
                        )
                    }

                    is OnboardingPageState.Page2State -> {
                        PreferFoodTypeSelectGrid(
                            modifier = Modifier.fillMaxSize(),
                            columnSize = currentState.columnSize,
                            foodItems = currentState.foodItems.toTypedArray(),
                            onCardClicked = onCardClicked,
                            isAllClicked = currentState.selectedCard.size >= 3,
                            selectedCard = currentState.selectedCard
                        )
                    }

                    is OnboardingPageState.Page3State -> {
                        FreqPlaceSelectGrid(
                            modifier = Modifier.fillMaxSize(),
                            columnSize = currentState.columnSize,
                            placeItems = currentState.placeItems.toTypedArray(),
                            onCardClicked = onCardClicked,
                            selectedCard = currentState.selectedCard
                        )
                    }

                    is OnboardingPageState.Page4State -> {
                        FreqPlaceSelectGrid(
                            modifier = Modifier.fillMaxSize(),
                            columnSize = currentState.columnSize,
                            placeItems = currentState.placeItems.toTypedArray(),
                            onCardClicked = onCardClicked,
                            selectedCard = currentState.selectedCard
                        )
                    }

                    is OnboardingPageState.Page5State -> {
                        PreferPlaceTypeSelectGrid(
                            modifier = Modifier.fillMaxSize(),
                            columnSize = currentState.columnSize,
                            placeItems = currentState.placeItems.toTypedArray(),
                            onCardClicked = onCardClicked,
                            selectedCard = currentState.selectedCard
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, start = 15.dp, end = 15.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            AconFilledLargeButton(
                text = "다음",
                textStyle = AconTheme.typography.head7_18_sb,
                enabledBackgroundColor = AconTheme.color.Gray5,
                disabledBackgroundColor =  AconTheme.color.Gray8,
                isEnabled = when (val currentState = screenState.currentState) {
                    is OnboardingPageState.Page1State -> currentState.selectedCard.isNotEmpty()
                    is OnboardingPageState.Page2State -> currentState.selectedCard.size == 3
                    is OnboardingPageState.Page3State -> currentState.selectedCard.isNotEmpty()
                    is OnboardingPageState.Page4State -> currentState.selectedCard.isNotEmpty()
                    is OnboardingPageState.Page5State -> currentState.selectedCard.size == 4
                },
                cornerRadius = 6.dp,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                onClick = { navigateToNextPage() }
            )
        }
    }
}

@Composable
@Preview
private fun PreviewOnboardingScreen(){
    OnboardingContainer(
    )
}
