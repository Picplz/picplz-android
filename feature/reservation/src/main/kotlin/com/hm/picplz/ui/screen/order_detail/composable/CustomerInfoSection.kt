package com.hm.picplz.ui.screen.order_detail.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor.Black
import com.hm.picplz.ui.theme.MainThemeColor.Gray4
import com.hm.picplz.ui.theme.MainThemeFont

@Composable
fun CustomerInfoSection(
    customerName: String,
    phoneNumber: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.order_detail_section_customer_info),
            style = MainThemeFont.ButtonDefault,
            color = Black,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(R.string.order_detail_label_customer_name),
            style = MainFontFamily.insideTag,
            color = Gray4,
        )

        Text(
            text = customerName,
            style = MainThemeFont.Body,
            color = Black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(R.string.order_detail_label_phone_number),
            style = MainFontFamily.insideTag,
            color = Gray4,
        )

        Text(
            text = phoneNumber,
            style = MainThemeFont.Body,
            color = Black,
        )
    }
}

@Preview
@Composable
private fun CustomerInfoSectionPreview() {
    CustomerInfoSection(
        customerName = "주은강",
        phoneNumber = "01012345678",
    )
}
