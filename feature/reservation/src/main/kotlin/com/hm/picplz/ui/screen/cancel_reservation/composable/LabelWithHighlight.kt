package com.hm.picplz.ui.screen.cancel_reservation.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun LabelWithHighlight(
    labelResId: Int,
    highlightResId: Int,
    highlightColor: Color,
    modifier: Modifier = Modifier,
) {
    val labelText = stringResource(labelResId)
    val highlightText = stringResource(highlightResId)

    val annotatedString =
        buildAnnotatedString {
            append("$labelText (")
            withStyle(style = SpanStyle(color = highlightColor)) {
                append(highlightText)
            }
            append(")")
        }

    Text(
        text = annotatedString,
        style = MainThemeFont.Caption,
        color = MainThemeColor.Gray4,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun LabelWithHighlightPreview() {
    PicplzTheme {
        LabelWithHighlight(
            labelResId = R.string.refund_cancellation_fee_label,
            highlightResId = R.string.refund_cancellation_fee_rate,
            highlightColor = Color(0xFFEF4747),
        )
    }
}
