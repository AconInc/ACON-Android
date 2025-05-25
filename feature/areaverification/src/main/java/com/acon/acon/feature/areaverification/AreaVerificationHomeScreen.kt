package com.acon.acon.feature.areaverification

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.innerShadow
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AreaVerificationHomeScreen() {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val offsetY = (screenHeightDp * 0.65f).dp
    val offsetBackGroundY = (screenHeightDp * 0.2f).dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray900)
    ) {
        AreaVerificationHomeBackGround(
            screenHeightDp = screenHeightDp,
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        x = 0,
                        y = offsetBackGroundY.roundToPx()
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(offsetY))
            Text(
                text = "믿을 수 있는 리뷰를 위해\n 지역인증이 필요해요",
                color = AconTheme.color.White,
                style = AconTheme.typography.Title1,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))
            Text(
                text = "더 정확한 로컬맛집을 추천해드릴 수 있어요",
                color = AconTheme.color.Gray50,
                style = AconTheme.typography.Body1
            )

            Spacer(Modifier.weight(1f))
            Text(
                text = "1초만에 인증하기",
                color = AconTheme.color.Gray500,
                style = AconTheme.typography.Body1,
            )

            AconFilledButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 36.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "다음",
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun AreaVerificationHomeBackGround(
    screenHeightDp: Int,
    modifier: Modifier = Modifier
) {
    val offsetY = (screenHeightDp * 0.33f).dp
    val glowEffectColor = AconTheme.color.GlowOrange
    val white = AconTheme.color.White

    Box(
        modifier = modifier
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.area_verification_home_bg),
            contentDescription = "배경",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .matchParentSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = offsetY)
            ) {
                Canvas(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center)
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowEffectColor,
                                Color.Transparent
                            )
                        ),
                        radius = size.minDimension / 2f,
                        center = center
                    )
                }
                Canvas(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Center)
                        .innerShadow(
                            shape = CircleShape,
                            color = AconTheme.color.GlowInnerShadow,
                            blur = 20.dp,
                            offsetY = 0.dp,
                            offsetX = 4.dp,
                            spread = 0.dp
                        )
                ) {
                    drawCircle(
                        color = white,
                        radius = size.minDimension / 2f,
                        style = Stroke(
                            width = 2.dp.toPx()
                        )
                    )
                }
            }
        }
    }
}