package com.hm.picplz.ui.screen.quick_shoot.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard

private const val MAX_DISPLAY_AREAS = 3

private fun formatActiveAreas(areas: List<String>): String {
    if (areas.isEmpty()) return ""
    val displayed = areas.take(MAX_DISPLAY_AREAS).joinToString(", ")
    val remaining = areas.size - MAX_DISPLAY_AREAS
    return if (remaining > 0) "$displayed 외 ${remaining}개" else displayed
}

@Composable
fun PhotographerCard(
    photographer: Photographer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .background(color = MainThemeColor.White)
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp),
    ) {
        AsyncImage(
            model = photographer.profileImageUri,
            contentDescription = "작가 카드 프로필",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(5.dp)),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .height(88.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = photographer.name,
                        style =
                            TextStyle(
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                lineHeight = 16.sp * 1.4,
                            ),
                        color = MainThemeColor.Black,
                        modifier = Modifier.weight(1f),
                    )
                    if (photographer.isActive) {
                        ActiveStatusBadge(text = "빠른촬영")
                    }
                }
                if (photographer.activeAreas.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatActiveAreas(photographer.activeAreas),
                        style =
                            TextStyle(
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 14.sp * 1.4,
                            ),
                        color = MainThemeColor.Gray4,
                    )
                }
            }
            val vibeTags = photographer.photoMoods.map { "#$it" }
            if (vibeTags.isNotEmpty()) {
                VibeTags(tags = vibeTags)
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun PhotographerCardPreview() {
    PhotographerCard(
        photographer =
            Photographer(
                id = 1,
                name = "유가영 작가",
                profileImageUri = "https://picsum.photos/200",
                isActive = true,
                distance = 100,
                photoMoods = listOf("을지로 감성", "MZ 감성", "MZ 감성"),
                activeAreas = listOf("마포구", "구로구", "노원구", "강남구", "서초구", "용산구"),
            ),
        onClick = {},
    )
}
