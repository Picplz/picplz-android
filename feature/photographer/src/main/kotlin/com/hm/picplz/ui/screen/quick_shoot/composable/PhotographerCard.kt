package com.hm.picplz.ui.screen.quick_shoot.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

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
                .padding(vertical = 16.dp),
    ) {
        AsyncImage(
            model = photographer.profileImageUri,
            contentDescription = "작가 카드 프로필",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp)),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = photographer.name,
                        style = MainThemeFont.BodyBold,
                        color = MainThemeColor.Black,
                    )
                    DistanceText(
                        distance = photographer.distance,
                    )
                }
                if (photographer.isActive) {
                    ActiveStatusBadge(text = "바로 촬영")
                }
            }
            val vibeTags =
                photographer.photoMoods.map { "#$it" }
            VibeTags(tags = vibeTags)
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
                name = "작가1",
                profileImageUri = "https://picsum.photos/200",
                isActive = true,
                distance = 100,
                photoMoods = listOf("을지로 감성", "키치 감성", "MZ 감성", "퇴폐 감성"),
            ),
        onClick = {},
    )
}
