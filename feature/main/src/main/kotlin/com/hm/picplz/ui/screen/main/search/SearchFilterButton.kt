package com.hm.picplz.ui.screen.main.search

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun SearchFilterButton(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .border(
                1.dp,
                if (isSelected) MainThemeColor.Black else MainThemeColor.Gray3,
                RoundedCornerShape(5.dp)
            )
            .clip(RoundedCornerShape(5.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MainThemeFont.Body.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                color = if (isSelected) MainThemeColor.Black else MainThemeColor.Gray4
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.triangle_down),
                contentDescription = "검색",
                modifier = Modifier.size(18.dp),
                tint = if (isSelected) MainThemeColor.Black else MainThemeColor.Gray3

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchFilterButtonSelectedPreview() {
    PicplzTheme {
        SearchFilterButton(label = "촬영 지역", isSelected = true)
    }
}

@Preview(showBackground = true)
@Composable
fun SearchFilterButtonUnSelectedPreview() {
    PicplzTheme {
        SearchFilterButton(label = "촬영 지역", isSelected = false)
    }
}