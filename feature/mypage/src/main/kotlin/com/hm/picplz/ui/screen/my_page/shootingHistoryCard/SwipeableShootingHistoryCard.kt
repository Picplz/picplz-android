package com.hm.picplz.ui.screen.my_page.shootingHistoryCard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeFont
import kotlin.math.abs

@Composable
fun SwipeableShootingHistoryCard(
    modifier: Modifier = Modifier,
    userName: String = "",
    userProfile: Int = R.drawable.default_profile,
    status: ShootingStatus = ShootingStatus.CANCLED,
    packageType: PackageType = PackageType.PROFILE,
    paymentDate: String = "2025.03.01",
    date: String = "",
    location: String = "",
    onDismiss: () -> Unit = {},
    onClickOrderSheet: () -> Unit = {},
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val maxSwipeDistance = with(LocalDensity.current) { 140.dp.toPx() } // 최대 140px까지만 스와이프

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        label = "offset_animation",
    )

    val deleteBackgroundAlpha by animateFloatAsState(
        targetValue = if (abs(offsetX) > 10f) 0.9f else 0f,
        label = "background_alpha",
    )

    Box(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clip(RoundedCornerShape(5.dp)),
        ) {
            // 삭제 영역 (오른쪽에 고정)
            Box(
                modifier =
                    Modifier
                        .height(253.dp)
                        .width(140.dp)
                        .align(Alignment.CenterEnd)
                        .background(
                            Color(0xFFEF4747).copy(alpha = deleteBackgroundAlpha),
                        )
                        .clickable {
                            if (abs(offsetX) >= maxSwipeDistance) {
                                onDismiss()
                            }
                        },
                contentAlignment = Alignment.Center,
            ) {
                if (deleteBackgroundAlpha > 0.5f) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.bin),
                            contentDescription = "Delete",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "내역 삭제️",
                            color = Color.White,
                            style = MainThemeFont.Caption,
                        )
                    }
                }
            }
        }

        // 메인 컨텐츠 (스와이프되는 부분)
        Box(
            modifier =
                Modifier
                    .offset(x = with(LocalDensity.current) { animatedOffsetX.toDp() })
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                // 드래그 종료 시 위치 결정
                                offsetX =
                                    when {
                                        offsetX < -maxSwipeDistance / 2 -> -maxSwipeDistance // 절반 이상 스와이프하면 완전히 열기
                                        offsetX > maxSwipeDistance / 2 -> maxSwipeDistance // 오른쪽으로 스와이프한 경우
                                        else -> 0f // 원래 위치로 복귀
                                    }
                            },
                        ) { _, dragAmount ->
                            // 왼쪽으로만 스와이프 가능하도록 제한
                            val newOffset = offsetX + dragAmount
                            offsetX =
                                when {
                                    newOffset > 0 -> 0f // 오른쪽으로는 스와이프 불가
                                    newOffset < -maxSwipeDistance -> -maxSwipeDistance // 최대 거리 제한
                                    else -> newOffset
                                }
                        }
                    },
        ) {
            ShootingHistoryCard(
                userName = userName,
                userProfile = userProfile,
                status = status,
                packageType = packageType,
                paymentDate = paymentDate,
                date = date,
                location = location,
                onClickOrderSheet = onClickOrderSheet,
            )
        }
    }
}
