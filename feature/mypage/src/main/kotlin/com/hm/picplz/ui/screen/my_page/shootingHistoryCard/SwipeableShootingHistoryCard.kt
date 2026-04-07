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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeFont
import kotlin.math.abs

private object SwipeableDefaults {
    val MaxSwipeWidth = 140.dp
    val DeleteAreaHeight = 253.dp
    val DeleteIconSize = 20.dp
    val CardCornerRadius = 5.dp
}

@Composable
fun SwipeableShootingHistoryCard(
    modifier: Modifier = Modifier,
    photographerName: String,
    photographerImageUri: String,
    productName: String,
    price: Int,
    status: ShootingStatus,
    paymentDate: String,
    shootingDate: String,
    shootingLocation: String,
    hasChatRoom: Boolean = true,
    onDismiss: () -> Unit = {},
    onClickChat: () -> Unit = {},
    onClickReview: () -> Unit = {},
    onClickOrderDetail: () -> Unit = {},
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val maxSwipeDistance = with(LocalDensity.current) { SwipeableDefaults.MaxSwipeWidth.toPx() }

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
                    .clip(RoundedCornerShape(SwipeableDefaults.CardCornerRadius)),
        ) {
            Box(
                modifier =
                    Modifier
                        .height(SwipeableDefaults.DeleteAreaHeight)
                        .width(SwipeableDefaults.MaxSwipeWidth)
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
                            contentDescription = stringResource(R.string.shooting_history_delete),
                            tint = Color.White,
                            modifier = Modifier.size(SwipeableDefaults.DeleteIconSize),
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.shooting_history_delete),
                            color = Color.White,
                            style = MainThemeFont.Caption,
                        )
                    }
                }
            }
        }

        Box(
            modifier =
                Modifier
                    .offset(x = with(LocalDensity.current) { animatedOffsetX.toDp() })
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                offsetX =
                                    when {
                                        offsetX < -maxSwipeDistance / 2 -> -maxSwipeDistance
                                        offsetX > maxSwipeDistance / 2 -> maxSwipeDistance
                                        else -> 0f
                                    }
                            },
                        ) { _, dragAmount ->
                            val newOffset = offsetX + dragAmount
                            offsetX =
                                when {
                                    newOffset > 0 -> 0f
                                    newOffset < -maxSwipeDistance -> -maxSwipeDistance
                                    else -> newOffset
                                }
                        }
                    },
        ) {
            ShootingHistoryCard(
                photographerName = photographerName,
                photographerImageUri = photographerImageUri,
                productName = productName,
                price = price,
                status = status,
                paymentDate = paymentDate,
                shootingDate = shootingDate,
                shootingLocation = shootingLocation,
                hasChatRoom = hasChatRoom,
                onClickChat = onClickChat,
                onClickReview = onClickReview,
                onClickOrderDetail = onClickOrderDetail,
            )
        }
    }
}
