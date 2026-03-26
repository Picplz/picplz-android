package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.feature.reservation.R as ReservationResource

@Composable
fun ReservationRefundPolicyDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier =
                modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MainThemeColor.White)
                    .padding(
                        top = 10.dp,
                        bottom = 20.dp,
                    ),
        ) {
            Icon(
                painter = painterResource(R.drawable.triangle_left),
                contentDescription = "arrow left",
                modifier =
                    Modifier
                        .padding(start = 10.dp)
                        .size(18.dp)
                        .clickable { onDismissRequest() },
            )

            Text(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                text = stringResource(ReservationResource.string.refund_policy_table_title),
                style = pretendardTypography.titleSmall,
                color = MainThemeColor.Black,
            )
            RefundPolicyTable(
                modifier =
                    Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun ReservationRefundPolicyDialogPreview() {
    ReservationRefundPolicyDialog(
        onDismissRequest = {},
    )
}
