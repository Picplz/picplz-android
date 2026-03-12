package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.detail_reservation.model.RefundCondition
import com.hm.picplz.ui.theme.MainFontFamily.buttonChat
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun RefundPolicyTable(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = MainThemeColor.Gray3,
                    shape = RoundedCornerShape(8.dp),
                ),
    ) {
        // 헤더
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .background(MainThemeColor.Black),
        ) {
            TableHeaderCell(
                text = stringResource(R.string.refund_policy_table_header_period),
                weight = 0.6f,
            )
            TableHeaderCell(
                text = stringResource(R.string.refund_policy_table_header_refund),
                weight = 0.4f,
            )
        }

        // 각 행
        RefundCondition.entries.forEach { condition ->
            HorizontalDivider(color = MainThemeColor.Gray3, thickness = 1.dp)

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
            ) {
                TableCell(
                    text = stringResource(condition.conditionResId),
                    weight = 0.6f,
                )
                VerticalDivider(color = MainThemeColor.Gray3, thickness = 1.dp)
                TableCell(
                    text =
                        if (condition.percent == 0) {
                            stringResource(R.string.refund_policy_no_refund)
                        } else {
                            "${condition.percent}%"
                        },
                    weight = 0.4f,
                    textColor = Color(0xFFFF7E7E),
                )
            }
        }
    }
}

@Composable
private fun RowScope.TableHeaderCell(
    text: String,
    weight: Float,
    textColor: Color = MainThemeColor.White,
) {
    Text(
        text = text,
        modifier =
            Modifier
                .weight(weight)
                .padding(vertical = 12.dp, horizontal = 8.dp),
        color = textColor,
        style = buttonChat,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
    textColor: Color = MainThemeColor.Gray4,
) {
    Text(
        text = text,
        modifier =
            Modifier
                .weight(weight)
                .padding(vertical = 12.dp, horizontal = 8.dp),
        color = textColor,
        style = caption,
        textAlign = TextAlign.Center,
    )
}

@Preview
@Composable
private fun RefundPolicyTablePreview() {
    RefundPolicyTable()
}
