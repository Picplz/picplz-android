package com.hm.picplz.ui.screen.chat_room.composable

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.roundToInt

private enum class Reveal { Closed, Open }

@Suppress("DEPRECATION")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlarmSwipe(
    isMuted: Boolean,
    onSwipe: () -> Unit,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val revealPx = with(density) { 61.dp.toPx() }

    val anchors =
        DraggableAnchors {
            Reveal.Closed at 0f
            Reveal.Open at -revealPx
        }

    val state =
        remember {
            AnchoredDraggableState(
                initialValue = Reveal.Closed,
                anchors = anchors,
                positionalThreshold = { it * 0.5f },
                velocityThreshold = { with(density) { 800.dp.toPx() } },
                snapAnimationSpec = tween(),
                decayAnimationSpec = androidx.compose.animation.splineBasedDecay(density),
            )
        }

    LaunchedEffect(state) {
        snapshotFlow { state.currentValue }
            .distinctUntilChanged()
            .collect { v ->
                if (v == Reveal.Open) {
                    state.snapTo(Reveal.Closed)
                    onSwipe()
                }
            }
    }

    val offsetX = if (state.anchors.size > 0) state.requireOffset() else 0f

    Box(
        Modifier
            .fillMaxWidth(),
    ) {
        Row(
            Modifier
                .matchParentSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .height(75.dp)
                        .width(61.dp)
                        .then(
                            if (isMuted) {
                                Modifier.background(MainThemeColor.Green120)
                            } else {
                                Modifier.background(MainThemeColor.Gray3)
                            },
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = if (isMuted) painterResource(R.drawable.bell_on) else painterResource(R.drawable.bell_off),
                        contentDescription = null,
                        modifier = Modifier.width(20.dp),
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        style = MainThemeFont.Caption,
                        text = if (isMuted) "알림on" else "알림off",
                        color = MainThemeColor.White,
                    )
                }
            }
        }

        Box(
            Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .anchoredDraggable(state = state, orientation = Orientation.Horizontal)
                .background(MainThemeColor.White),
        ) {
            content()
        }
    }
}
