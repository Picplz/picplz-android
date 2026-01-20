package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.domain.model.Area
import com.hm.picplz.ui.screen.common.CommonCheckbox
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun AreaListItem(
    modifier: Modifier = Modifier,
    area: Area,
    isSelected: Boolean = false,
    onItemClick: (Area) -> Unit,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onItemClick(area) },
        colors =
            CardDefaults.cardColors(
                containerColor = MainThemeColor.White,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = area.name,
                style = pretendardTypography.bodyMedium,
                color = MainThemeColor.Black,
            )
            CommonCheckbox(
                checked = isSelected,
                onCheckedChange = { onItemClick(area) },
            )
        }
    }
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
fun AreaListItemCheckedPreview() {
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
