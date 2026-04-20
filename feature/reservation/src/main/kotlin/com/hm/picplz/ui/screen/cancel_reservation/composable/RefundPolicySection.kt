package com.hm.picplz.ui.screen.cancel_reservation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun RefundPolicySection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.refund_policy_title),
            style = MainThemeFont.ButtonDefault,
            color = MainThemeColor.Black,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        Text(
            text = stringResource(R.string.refund_policy_list),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RefundPolicySectionPreview() {
    PicplzTheme {
        RefundPolicySection()
    }
}
