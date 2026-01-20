package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun CommonButtonModal(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    width: Dp = 304.dp,
    confirmText: String = "확인",
    cancelText: String = "취소",
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-30).dp)
                .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDismissRequest() },
                contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = modifier
                    .width(width)
                    .border(
                        width = 1.dp,
                        color = MainThemeColor.Gray2,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {},
                shape = RoundedCornerShape(5.dp),
            ) {
                Column {
                    content()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .drawBehind {
                                drawLine(
                                    color = MainThemeColor.Gray3,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                    ) {
                        Button(
                            onClick = onCancel,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 5.dp,
                                bottomEnd = 0.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MainThemeColor.White,
                                contentColor = MainThemeColor.Black,
                            )
                        ) {
                            Text(
                                text = cancelText,
                                style = pretendardTypography.titleSmall,
                            )
                        }
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 5.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MainThemeColor.Black,
                                contentColor = MainThemeColor.White,
                            )
                        ) {
                            Text(
                                text = confirmText,
                                style = pretendardTypography.titleSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonButtonModalPreview() {
    CommonButtonModal() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.marker_map),
                    contentDescription = "마커 이미지",
                    modifier = Modifier
                        .width(49.dp)
                        .height(62.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "2km 이내 고객에게\n내 위치 정보가 나타납니다",
                    textAlign = TextAlign.Center,
                    style = pretendardTypography.titleSmall
                )
            }
        }
    }
}