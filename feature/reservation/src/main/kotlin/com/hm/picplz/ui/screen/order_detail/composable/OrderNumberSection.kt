package com.hm.picplz.ui.screen.order_detail.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor.Black
import com.hm.picplz.ui.theme.MainThemeColor.Gray4
import com.hm.picplz.ui.theme.MainThemeFont

@Composable
fun OrderNumberSection(
    orderNumber: String,
    orderTime: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(R.string.order_detail_section_order_number),
            style = MainThemeFont.ButtonDefault,
            color = Black,
        )

        Text(
            text = orderNumber,
            style = MainThemeFont.BodyBold,
            color = Color(0XFFEF4747),
        )

        Text(
            text = orderTime,
            style = MainThemeFont.BodyBold,
            color = Gray4,
        )
    }
}

@Preview(widthDp = 340)
@Composable
private fun OrderNumberSectionPreview() {
    OrderNumberSection(
        orderNumber = "nnnnmmdd123456",
        orderTime = "2025-03-09 19:09:14",
    )
}
