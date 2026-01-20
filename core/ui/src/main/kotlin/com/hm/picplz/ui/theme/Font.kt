package com.hm.picplz.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hm.picplz.core.ui.R

val Pretendard =
    FontFamily(
        Font(R.font.pretendard_black, FontWeight.Black),
        Font(R.font.pretendard_bold, FontWeight.Bold),
        Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
        Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
        Font(R.font.pretendard_light, FontWeight.Light),
        Font(R.font.pretendard_medium, FontWeight.Medium),
        Font(R.font.pretendard_regular, FontWeight.Normal),
        Font(R.font.pretendard_semibold, FontWeight.SemiBold),
        Font(R.font.pretendard_thin, FontWeight.Thin),
    )

val pretendardTypography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 16.sp * 1.4,
                letterSpacing = 0.5.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 14.sp * 1.4,
                letterSpacing = 0.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.0.sp,
                letterSpacing = 0.4.sp,
            ),
        displayLarge =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 57.sp,
                lineHeight = 64.0.sp,
                letterSpacing = (-0.2).sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 45.sp,
                lineHeight = 52.0.sp,
                letterSpacing = 0.0.sp,
            ),
        displaySmall =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 36.sp,
                lineHeight = 44.0.sp,
                letterSpacing = 0.0.sp,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
                lineHeight = 40.0.sp,
                letterSpacing = 0.0.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 32.0.sp,
                letterSpacing = 0.0.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.0.sp,
                letterSpacing = 0.1.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.0.sp,
                letterSpacing = 0.5.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 16.0.sp,
                letterSpacing = 0.5.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 24.sp * 1.4,
                letterSpacing = 0.0.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 20.sp * 1.4,
                letterSpacing = 0.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 18.sp * 1.4,
                letterSpacing = 0.sp,
            ),
    )

val buttonText =
    TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 16.sp * 1.4,
        letterSpacing = 0.sp,
    )

object MainFontFamily {
    // 캡션
    val caption =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 12.sp * 1.4,
            letterSpacing = 0.sp,
        )
    val titleMediumLarge =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 22.sp * 1.4,
            letterSpacing = 0.sp,
        )
    val titleLarge =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 24.sp * 1.4,
            letterSpacing = 0.0.sp,
        )

    // 큰 본문
    val bodyLarge =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 16.sp * 1.4,
            letterSpacing = 0.sp,
        )

    // 굵은 본문
    val bodyBold =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 14.sp * 1.4,
            letterSpacing = 0.sp,
        )

    val buttonChat =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 12.sp * 1.4,
            letterSpacing = 0.sp,
        )

    val insideTag =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 12.sp * 1.4,
            letterSpacing = 0.sp,
        )

    val buttonDefault =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 12.sp * 1.4,
            letterSpacing = 0.sp,
        )
}

object MainThemeFont {
    /** 큰 제목 · 24/140 */
    val TitleLarge =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
        )

    /** 제목 · 20/140 */
    val Title =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
        )

    /** 작은 제목 · 18/140 */
    val TitleSmall =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 26.sp,
        )

    /** 큰 본문 · 16/140 */
    val BodyLarge =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )

    /** 굵은 본문 · 14/140 */
    val BodyBold =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        )

    /** 본문 · 14/140 */
    val Body =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        )

    /** 더 작은 본문 · 13/140 */
    val BodySmaller =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 21.sp,
        )

    /** 본문, 작은 버튼 2 · 15/140 */
    val BodySmallButton2 =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            lineHeight = 23.sp,
        )

    /** 캡션 · 12/140 */
    val Caption =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 18.sp,
        )

    /** 태그 내부 · 12/140 */
    val InnerTag =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        )

    /** 버튼 기본 · 16/140 */
    val ButtonDefault =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 22.sp,
        )

    /** 버튼 채팅 · 12/140 */
    val ButtonChat =
        TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        )
}
