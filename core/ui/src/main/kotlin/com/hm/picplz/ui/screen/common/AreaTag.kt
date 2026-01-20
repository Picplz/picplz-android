package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun AreaTag(
    modifier: Modifier = Modifier,
    label: String,
    onRemove: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(28.dp)
            .background(
                color = MainThemeColor.White,
                shape = RoundedCornerShape(5.dp)
            )
            .border(
                width = 1.dp,
                color = MainThemeColor.Black,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(horizontal = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MainThemeFont.BodyBold,
        )

        Spacer(modifier = Modifier.width(6.dp))

        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(14.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.full_close),
                contentDescription = "삭제",
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AreaTagPreview() {
    PicplzTheme {
        AreaTag(
            label = "연희동",
            onRemove = {}
        )
    }
}