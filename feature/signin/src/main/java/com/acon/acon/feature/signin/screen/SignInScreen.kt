package com.acon.acon.feature.signin.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.button.AconGoogleLoginButton
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.signin.R
import com.acon.acon.feature.signin.amplitude.amplitudeSignIn
import com.acon.acon.feature.signin.screen.component.SignInTopBar
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(
    state: SignInUiState,
    modifier: Modifier = Modifier,
    navigateToSpotListView: () -> Unit,
    onClickTermsOfUse: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
    onClickLoginGoogle: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity

    BackHandler(enabled = true) { activity?.finish() }

    when(state) {
        is SignInUiState.Loading -> {
            Column(
                modifier = modifier
                    .background(AconTheme.color.Gray9),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.Asset("acon_splash_lottie.json")
                )
                var startAnimation by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(1000)
                    startAnimation = true
                }
                val logoAnimationState = animateLottieCompositionAsState(
                    composition = if (startAnimation) composition else null
                )
                val alpha by animateFloatAsState(
                    targetValue = if (logoAnimationState.value >= .99f) 1f else 0f,
                    animationSpec = tween(1000),
                    label = stringResource(R.string.splash_text_animation_label)
                )

                SignInTopBar(
                    modifier = Modifier
                        .padding(top = 42.dp)
                        .alpha(alpha),
                    onClickText = { if (alpha >= 0.75f) navigateToSpotListView() }
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(bottom = 200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = {
                            logoAnimationState.progress
                        },
                        modifier = Modifier
                            .width(width = 320.dp)
                    )
                }

                AconGoogleLoginButton(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 16.dp)
                        .alpha(alpha),
                    onClick = {
                        if (alpha >= 0.75f) {
                            onClickLoginGoogle()
                            amplitudeSignIn()
                        }
                    }
                )
                Text(
                    text = stringResource(R.string.signin_policy),
                    style = AconTheme.typography.body2_14_reg,
                    color = AconTheme.color.Gray3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .alpha(alpha)
                )
                Row(
                    modifier = Modifier
                        .padding(bottom = 76.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.signin_terms_of_service),
                        style = AconTheme.typography.body2_14_reg,
                        color = AconTheme.color.Gray5,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .noRippleClickable { if (alpha >= 0.75f) onClickTermsOfUse() }
                            .alpha(alpha)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.signin_privacy_policy),
                        style = AconTheme.typography.body2_14_reg,
                        color = AconTheme.color.Gray5,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .noRippleClickable { if (alpha >= 0.75f) onClickPrivacyPolicy() }
                            .alpha(alpha)
                    )
                }
            }
        }
        is SignInUiState.Success -> {}
        is SignInUiState.LoadFailed -> {}
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSignInScreen() {
    AconTheme {
        SignInScreen(
            state = SignInUiState.Loading,
            navigateToSpotListView = {},
            onClickTermsOfUse = {},
            onClickPrivacyPolicy = {},
            onClickLoginGoogle = {}
        )
    }
}