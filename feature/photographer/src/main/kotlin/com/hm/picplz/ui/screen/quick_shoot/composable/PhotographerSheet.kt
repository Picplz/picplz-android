package com.hm.picplz.ui.screen.quick_shoot.composable

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.ui.screen.quick_shoot.QuickShootViewModel
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard

@Composable
fun PhotographerSheet(
    viewModel: QuickShootViewModel = hiltViewModel(),
    mainNavController: NavController,
) {
    val currentState = viewModel.state.collectAsState().value
    val selectedPhotographer =
        currentState.selectedPhotographerId?.let { selectedId ->
            currentState.nearbyPhotographers.let { photographers ->
                (photographers.active + photographers.inactive).find { it.id == selectedId }
            }
        }
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable {
                    selectedPhotographer?.let {
                        mainNavController.navigate(DetailPhotographer(it.id.toInt()))
                    }
                },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = selectedPhotographer?.profileImageUri),
                contentDescription = "작가 프로필 이미지",
                modifier =
                    Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(1.dp, MainThemeColor.Gray2, CircleShape),
            )
            Text(
                text = selectedPhotographer?.name ?: "",
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
            ActiveStatusBadge(text = "바로 촬영")
            DistanceText(
                distance = selectedPhotographer?.distance.toString(),
                duration = "도보 10분 거리",
                modifier =
                    Modifier
                        .padding(start = 4.dp),
            )
        }
        val vibeTags = selectedPhotographer?.photoMoods?.map { "#$it" } ?: emptyList()
        VibeTags(
            modifier = Modifier.padding(top = 20.dp),
            tags = vibeTags,
        )
    }
}
