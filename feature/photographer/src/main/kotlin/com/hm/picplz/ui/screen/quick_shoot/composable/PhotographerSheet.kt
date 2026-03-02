package com.hm.picplz.ui.screen.quick_shoot.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard

@Composable
fun PhotographerSheet(
    photographer: Photographer,
    onNavigateToDetail: (Long) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onNavigateToDetail(photographer.id)
                }
                .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = photographer.profileImageUri,
                contentDescription = "작가 프로필 이미지",
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, MainThemeColor.Gray2, CircleShape),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = photographer.name,
                style =
                    TextStyle(
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        letterSpacing = 0.sp,
                    ),
                color = MainThemeColor.Black,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (photographer.isActive) {
                ActiveStatusBadge(text = "바로촬영")
                Spacer(modifier = Modifier.width(6.dp))
            }
            val areasText = formatActiveAreas(photographer.activeAreas)
            if (areasText.isNotEmpty()) {
                Text(
                    text = areasText,
                    style =
                        TextStyle(
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            letterSpacing = 0.sp,
                        ),
                    color = MainThemeColor.Gray4,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        VibeTags(
            tags = photographer.photoMoods,
        )
    }
}

private fun formatActiveAreas(areas: List<String>): String {
    if (areas.isEmpty()) return ""
    val displayCount = 3
    val displayed = areas.take(displayCount).joinToString(", ")
    val remaining = areas.size - displayCount
    return if (remaining > 0) {
        "$displayed 외 ${remaining}개"
    } else {
        displayed
    }
}
