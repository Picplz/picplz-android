package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.domain.model.Area
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun AreaListItem(
    modifier: Modifier = Modifier,
    area: Area,
    isSelected: Boolean = false,
    onItemClick: (Area) -> Unit,
) {
    Text(
        text = area.name,
        style = if (isSelected) MainThemeFont.BodyBold else MainThemeFont.Body,
        color = MainThemeColor.Black,
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onItemClick(area) }
                .padding(vertical = 16.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun AreaListItemPreview() {
    PicplzTheme {
        AreaListItem(
            area =
                Area(
                    id = 1,
                    name = "서울 서대문구 연희동",
                    dong = "무악",
                    ri = null,
                ),
            onItemClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AreaListItemSelectedPreview() {
    PicplzTheme {
        AreaListItem(
            area =
                Area(
                    id = 1,
                    name = "서울 서대문구 연희동",
                    dong = "무악",
                    ri = null,
                ),
            onItemClick = {},
            isSelected = true,
        )
    }
}
