package com.acon.acon.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.acon.acon.core.designsystem.R

private val Pretendard = FontFamily(
    Font(resId = R.font.pretendard_bold, weight = FontWeight.W700),
    Font(resId = R.font.pretendard_extrabold, weight = FontWeight.ExtraBold),
    Font(resId = R.font.pretendard_light, weight = FontWeight.Light),
    Font(resId = R.font.pretendard_medium, weight = FontWeight.W500),
    Font(resId = R.font.pretendard_regular, weight = FontWeight.W400),
    Font(resId = R.font.pretendard_semibold, weight = FontWeight.W600),
    Font(resId = R.font.pretendard_thin, weight = FontWeight.Thin),
)

internal val Typography = AconTypography(
    //headline
    head1_32_sb = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 32.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.023).em
    ),
    head2_28_sb = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 28.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.023).em
    ),
    head3_26_sb = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 26.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.023).em
    ),
    head4_24_sb = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.023).em
    ),
    head5_22_sb = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.023).em
    ),
    head6_20_sb = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.023).em
    ),
    head7_18_sb = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.023).em
    ),
    head8_16_sb = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.023).em
    ),
    //title
    title1_24_b = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.023).em
    ),
    title2_20_b = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.023).em
    ),
    title3_18_b = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.023).em
    ),
    //subtitle
    subtitle1_16_med = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.023).em
    ),
    subtitle2_14_med = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.023).em
    ),
    //body
    body1_15_reg = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.023).em
    ),
    body2_14_reg = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.023).em
    ),
    body3_13_reg = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.023).em
    ),
    body4_12_reg = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.023).em
    ),
    //caption
    cap1_11_reg = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.023).em
    ),

    Headline1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.025).em
    ),
    Headline2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.025).em
    ),
    Headline3 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.025).em
    ),
    Headline4 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.025).em
    ),

    Title1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.025).em
    ),
    Title2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.025).em
    ),
    Title3 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.025).em
    ),
    Title4 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.025).em
    ),
    Title5 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.025).em
    ),

    Body1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.025).em
    ),

    Caption1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.025).em
    ),
    Caption2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.025).em
    ),
)

@Immutable
data class AconTypography(
    val head1_32_sb: TextStyle,
    val head2_28_sb: TextStyle,
    val head3_26_sb: TextStyle,
    val head4_24_sb: TextStyle,
    val head5_22_sb: TextStyle,
    val head6_20_sb: TextStyle,
    val head7_18_sb: TextStyle,
    val head8_16_sb: TextStyle,

    val title1_24_b: TextStyle,
    val title2_20_b: TextStyle,
    val title3_18_b: TextStyle,

    val subtitle1_16_med: TextStyle,
    val subtitle2_14_med: TextStyle,

    val body1_15_reg: TextStyle,
    val body2_14_reg: TextStyle,
    val body3_13_reg: TextStyle,
    val body4_12_reg: TextStyle,

    val cap1_11_reg: TextStyle,

    // Acon 2.0
    val Headline1: TextStyle,
    val Headline2: TextStyle,
    val Headline3: TextStyle,
    val Headline4: TextStyle,

    val Title1: TextStyle,
    val Title2: TextStyle,
    val Title3: TextStyle,
    val Title4: TextStyle,
    val Title5: TextStyle,

    val Body1: TextStyle,

    val Caption1: TextStyle,
    val Caption2: TextStyle,

)

internal val LocalAconTypography = staticCompositionLocalOf {
    Typography
}
