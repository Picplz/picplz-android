package com.hm.picplz.ui.screen.cancel_reservation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.screen.common.CommonCheckbox
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun CheckboxWithLabel(
    text: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CommonCheckbox(
            checked = isSelected,
            onCheckedChange = { onToggle() },
        )

        Text(
            text = text,
            style = if (isSelected) MainThemeFont.BodyBold else MainThemeFont.Body,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckboxWithLabelPreviewUnchecked() {
    PicplzTheme {
        CheckboxWithLabel(
            text = "촬영 스케줄 다른 일정이 생겼어요",
            isSelected = false,
            onToggle = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckboxWithLabelPreviewChecked() {
    PicplzTheme {
        CheckboxWithLabel(
            text = "촬영 스케줄 다른 일정이 생겼어요",
            isSelected = true,
            onToggle = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckboxWithLabelPreviewMultiple() {
    PicplzTheme {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            CheckboxWithLabel(
                text = "촬영 스케줄 다른 일정이 생겼어요",
                isSelected = true,
                onToggle = {},
                modifier = Modifier.padding(vertical = 12.dp),
            )

            CheckboxWithLabel(
                text = "원금 상품을 변경하고 싶어요",
                isSelected = false,
                onToggle = {},
                modifier = Modifier.padding(vertical = 12.dp),
            )

            CheckboxWithLabel(
                text = "그냥 마음이 바뀌었어요",
                isSelected = true,
                onToggle = {},
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }
    }
}
