package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

private object CommonDestructiveConfirmDialogDefaults {
    val Width = 304.dp
    val Radius = RoundedCornerShape(5.dp)
    val ContentHorizontalPadding = 36.dp
    val ContentTopPadding = 18.dp
    val ContentBottomPadding = 18.dp
    val TitleBodyGap = 10.dp
    val ButtonHeight = 46.dp
    val ScrimColor = Color(0x66000000)
}

@Composable
fun CommonDestructiveConfirmDialog(
    title: String,
    description: String,
    cancelText: String,
    confirmText: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(CommonDestructiveConfirmDialogDefaults.ScrimColor)
                    .clickable(onClick = onDismissRequest),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier =
                    modifier
                        .width(CommonDestructiveConfirmDialogDefaults.Width)
                        .clickable(enabled = false, onClick = {}),
                shape = CommonDestructiveConfirmDialogDefaults.Radius,
                color = MainThemeColor.White,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = CommonDestructiveConfirmDialogDefaults.ContentHorizontalPadding,
                                    end = CommonDestructiveConfirmDialogDefaults.ContentHorizontalPadding,
                                    top = CommonDestructiveConfirmDialogDefaults.ContentTopPadding,
                                    bottom = CommonDestructiveConfirmDialogDefaults.ContentBottomPadding,
                                ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = title,
                            style = MainThemeFont.TitleSmall,
                            color = MainThemeColor.Black,
                            textAlign = TextAlign.Center,
                        )
                        Box(modifier = Modifier.height(CommonDestructiveConfirmDialogDefaults.TitleBodyGap))
                        Text(
                            text = description,
                            style = MainThemeFont.Body,
                            color = MainThemeColor.Gray5,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(CommonDestructiveConfirmDialogDefaults.ButtonHeight),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .background(MainThemeColor.White)
                                    .clickable(onClick = onDismissRequest),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .align(Alignment.TopCenter)
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(MainThemeColor.Gray2),
                            )
                            Text(
                                text = cancelText,
                                style = MainThemeFont.ButtonDefault,
                                color = MainThemeColor.Black,
                            )
                        }
                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .background(Color.Black)
                                    .clickable(onClick = onConfirm),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = confirmText,
                                style = MainThemeFont.ButtonDefault,
                                color = MainThemeColor.White,
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
private fun CommonDestructiveConfirmDialogPreview() {
    PicplzTheme {
        CommonDestructiveConfirmDialog(
            title = "리뷰를 삭제하시겠습니까?",
            description = "삭제된 리뷰는\n되돌릴 수 없습니다.",
            cancelText = "취소",
            confirmText = "삭제",
            onDismissRequest = {},
            onConfirm = {},
        )
    }
}
