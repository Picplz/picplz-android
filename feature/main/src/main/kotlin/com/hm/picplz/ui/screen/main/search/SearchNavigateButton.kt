package com.hm.picplz.ui.screen.main.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun SearchNavigateButton(
    modifier: Modifier = Modifier,
    placeholder: String,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(50.dp))
            .background(MainThemeColor.White)
            .padding(horizontal = 18.dp, vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = placeholder, style = MainThemeFont.Body, color = MainThemeColor.Gray3)
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "검색",
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchNavigateButtonPreview() {
    PicplzTheme {
        SearchNavigateButton(
            placeholder = "촬영을 하고 싶은 장소 또는 동을 검색해보세요",
            onClick = {}
        )
    }
}