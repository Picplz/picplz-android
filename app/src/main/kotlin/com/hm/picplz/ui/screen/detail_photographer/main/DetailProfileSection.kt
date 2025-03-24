package com.hm.picplz.ui.screen.detail_photographer.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.R
import com.hm.picplz.data.model.PhotographerInfo
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.screen.search_photographer.composable.ActiveStatusBadge
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun DetailProfileSection(modifier: Modifier, profileInfo: PhotographerInfo) {
    val uriHandler = LocalUriHandler.current
    var isFollow by remember { mutableStateOf(profileInfo.isFollow) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = profileInfo.followCount.toString())
        Spacer(modifier = Modifier.width(6.dp))
        CommonIconButton(
            label = if (isFollow) "팔로잉" else "팔로우",
            backgroundColor = if (isFollow) MainThemeColor.Black else MainThemeColor.Gray2,
            textColor = if (isFollow) MainThemeColor.White else MainThemeColor.Gray4,
            iconResId = if (isFollow) R.drawable.following else R.drawable.follow,
            location = "right",
            horizontalPadding = 7.dp,
            verticalPadding = 3.dp,
            borderRadius = 5.dp,
            onClick = {
                // 버튼 클릭 시 상태 변경
                // TODO: 서버 API 연동
                isFollow = !isFollow
            }
        )

    }

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Image(
            rememberAsyncImagePainter(model = profileInfo.profileImageUri),
            contentDescription = "작가 프로필",
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
                .border(1.dp, MainThemeColor.Gray2, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row {
                Text(text = "${profileInfo.name} 작가", style = pretendardTypography.titleSmall)
                Spacer(modifier = Modifier.width(9.dp))
                if (profileInfo.isActive) {
                    ActiveStatusBadge(text = "바로 촬영")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.instagram),
                    contentDescription = "별점",
                )
                Spacer(modifier = Modifier.width(3.dp))
                ClickableText(
                    text = AnnotatedString(profileInfo.socialAccount.toString()),
                    style = pretendardTypography.bodySmall.copy(
                        color = MainThemeColor.Black,
                        textDecoration = TextDecoration.Underline
                    ),
                    onClick = {
                        uriHandler.openUri("https://www.instagram.com/${profileInfo.socialAccount}/")
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = profileInfo.infoText,
                style = pretendardTypography.labelSmall
            )
        }
    }

    Divider(
        color = MainThemeColor.Gray2,
        thickness = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    )
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "촬영지",
            style = pretendardTypography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(16.dp))
        profileInfo.workingArea.forEachIndexed { index, area ->
            Text(
                text = area,
                style = pretendardTypography.labelMedium,
                color = MainThemeColor.Gray4
            )
            if (index != profileInfo.keyword.lastIndex) {
                Text(
                    text = ", ",
                    style = pretendardTypography.labelMedium,
                    color = MainThemeColor.Gray4
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "키워드",
            style = pretendardTypography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(16.dp))
        profileInfo.keyword.forEachIndexed { index, keyword ->
            Text(
                text = keyword,
                style = pretendardTypography.labelMedium,
                color = MainThemeColor.Gray4
            )
            if (index != profileInfo.keyword.lastIndex) {
                Text(
                    text = ", ",
                    style = pretendardTypography.labelMedium,
                    color = MainThemeColor.Gray4
                )
            }
        }
    }
}