package com.hm.picplz.ui.screen.quick_shoot.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
                .padding(top = 12.dp)
                .clickable {
                    onNavigateToDetail(photographer.id)
                },
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
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(1.dp, MainThemeColor.Gray2, CircleShape),
            )
            Text(
                text = photographer.name,
                style =
                    TextStyle(
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        letterSpacing = 0.sp,
                    ),
                modifier =
                    Modifier
                        .padding(start = 4.dp),
            )
        }
        Row(
            modifier =
                Modifier
                    .padding(start = 4.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (photographer.isActive) {
                ActiveStatusBadge(text = "바로 촬영")
            }
            DistanceText(
                distance = photographer.distance,
                modifier =
                    Modifier
                        .padding(start = 4.dp),
            )
        }
        val vibeTags = photographer.photoMoods.map { "#$it" }
        VibeTags(
            modifier = Modifier.padding(top = 20.dp),
            tags = vibeTags,
        )
    }
}
