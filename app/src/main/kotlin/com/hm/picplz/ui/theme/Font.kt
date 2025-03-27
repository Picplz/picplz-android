package com.hm.picplz.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hm.picplz.R

val Pretendard = FontFamily(
    Font(R.font.pretendard_black, FontWeight.Black),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_thin, FontWeight.Thin)
)

val pretendardTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 16.sp * 1.4,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 14.sp * 1.4,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.4.sp
    ),
    displayLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.0.sp,
        letterSpacing = (-0.2).sp
    ),
    displayMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.0.sp,
        letterSpacing = 0.0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.0.sp,
        letterSpacing = 0.0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.0.sp,
        letterSpacing = 0.0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.0.sp,
        letterSpacing = 0.0.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp * 1.4,
        letterSpacing = 0.0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 20.sp * 1.4,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 18.sp * 1.4,
        letterSpacing = 0.sp
    )
)

val MainTypography = Typography(
    displayLarge = Typography().displayLarge.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
    ),
    displayMedium = Typography().displayMedium.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
    ),
    displaySmall = Typography().displaySmall.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
    ),

    headlineLarge = Typography().headlineLarge.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
    ),
    headlineMedium = Typography().headlineMedium.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
    ),
    headlineSmall = Typography().headlineSmall.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
    ),

    // 제목
    titleLarge = Typography().titleLarge.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    titleMedium = Typography().titleMedium.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
    titleSmall = Typography().titleSmall.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),

    // 본문
    bodyLarge = Typography().bodyLarge.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    bodyMedium = Typography().bodyMedium.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    bodySmall = Typography().bodySmall.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
    ),

    labelLarge = Typography().labelLarge.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
    ),
    labelMedium = Typography().labelMedium.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
    ),
    labelSmall = Typography().labelSmall.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
    ),
)

// 캡션
val Typography.caption: TextStyle
    get() = Typography().labelMedium.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
    )

// 버튼
val Typography.button: TextStyle
    get() = Typography().bodyLarge.copy(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )
