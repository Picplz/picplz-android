package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ActiveAreaListItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        style = if (isSelected) MainThemeFont.BodyBold else MainThemeFont.Body,
        color = MainThemeColor.Black,
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 16.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun ActiveAreaListItemPreview() {
    PicplzTheme {
        ActiveAreaListItem(
            label = "서울 서대문구 연희동",
            isSelected = false,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ActiveAreaListItemSelectedPreview() {
    PicplzTheme {
        ActiveAreaListItem(
            label = "서울 서대문구 연희동",
            isSelected = true,
            onClick = {},
        )
    }
}
