package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun CommonCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clickable { onCheckedChange(!checked) }
            .background(
                color = if (checked) MainThemeColor.Black else MainThemeColor.Gray2,
                shape = RoundedCornerShape(2.dp)
            )
            .border(
                width = 1.dp,
                color =  if (checked) MainThemeColor.Black else MainThemeColor.Gray3,
                shape =  RoundedCornerShape(2.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.check),
            contentDescription = "체크 아이콘",
            tint = MainThemeColor.White,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonCheckboxPreview() {
    PicplzTheme {
        CommonCheckbox(checked = true, onCheckedChange = {})
    }
}

@Preview(showBackground = true)
@Composable
fun CommonCheckboxFalsePreview() {
    PicplzTheme {
        CommonCheckbox(checked = false, onCheckedChange = {})
    }
}