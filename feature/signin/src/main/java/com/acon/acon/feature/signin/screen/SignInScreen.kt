package com.acon.acon.feature.signin.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.AconGoogleLoginButton
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.signin.amplitude.amplitudeSignIn
import com.acon.acon.feature.signin.screen.component.SignInTopBar
import com.acon.acon.feature.signin.utils.SplashAudioManager
import com.acon.feature.common.compose.getScreenHeight
import com.acon.feature.common.compose.getScreenWidth
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
    onAnimationEnd:() -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val splashAudioManager = remember { SplashAudioManager(context) }
    val screenWidth = getScreenWidth()
    val screenHeight = getScreenHeight()
    val lottieTopPadding = (screenHeight * (280f / 740f))

    var isEndShowImage by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("acon_splash_lottie_json_v2.json")
    )
    val logoAnimationState = animateLottieCompositionAsState(composition = composition)
    val alpha by animateFloatAsState(
        targetValue = if (isEndShowImage && logoAnimationState.value >= .99f) 1f else 0f,
        animationSpec = tween(400),
        label = stringResource(R.string.splash_animation_content_description)
    )

    BackHandler(enabled = true) {
        activity?.finishAffinity()
    }

    LaunchedEffect(alpha) {
        if (alpha == 1f) {
            onAnimationEnd()
        }
    }

    LaunchedEffect(logoAnimationState.isPlaying) {
        if (logoAnimationState.isPlaying) {
            delay(900)
            isEndShowImage = true
        }
    }

    LaunchedEffect(logoAnimationState.isPlaying) {
        if (logoAnimationState.isPlaying) {
            splashAudioManager.playSplashSoundIfAllowed()
        } else {
            splashAudioManager.release()
        }
    }

    when(state) {
        is SignInUiState.SignIn -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Gray900)
                    .navigationBarsPadding()
            ) {
                SignInTopBar(
                    modifier = Modifier
                        .padding(top = 42.dp)
                        .alpha(alpha),
                    onClickText = {
                        if (alpha >= 0.75f) {
                            navigateToSpotListView()
                        }
                    }
                )

                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = {
                            logoAnimationState.progress
                        },
                        modifier = Modifier
                            .width(width = (screenWidth * (240f / 360f)))
                            .padding(top = lottieTopPadding)
                    )

                    Spacer(Modifier.height(60.dp))
                    if (isEndShowImage) {
                        Image(
                            painter = painterResource(R.drawable.ic_splash_shadow),
                            contentDescription = stringResource(R.string.splash_animation_content_description),
                        )
                    }

                    Spacer(Modifier.weight(1f))
                    AconGoogleLoginButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .alpha(alpha),
                        onClick = {
                            if (alpha >= 0.75f) {
                                onClickLoginGoogle()
                                amplitudeSignIn()
                            }
                        }
                    )

                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.signin_policy),
                        style = AconTheme.typography.Caption1,
                        color = AconTheme.color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .alpha(alpha)
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.signin_terms_of_service),
                            color = AconTheme.color.White,
                            style = AconTheme.typography.Caption1,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .noRippleClickable {
                                    if (alpha >= 0.75f) {
                                        onClickTermsOfUse()
                                    }
                                }
                                .alpha(alpha)
                        )

                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = stringResource(R.string.signin_privacy_policy),
                            color = AconTheme.color.White,
                            style = AconTheme.typography.Caption1,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .noRippleClickable {
                                    if (alpha >= 0.75f) {
                                        onClickPrivacyPolicy()
                                    }
                                }
                                .alpha(alpha)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSignInScreen() {
    AconTheme {
        SignInScreen(
            state = SignInUiState.SignIn,
            navigateToSpotListView = {},
            onClickTermsOfUse = {},
            onClickPrivacyPolicy = {},
            onClickLoginGoogle = {},
            onAnimationEnd = {}
        )
    }
}