package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun CommonReviewEmptyState(modifier: Modifier = Modifier) {
    CommonEmptyState(
        title = stringResource(R.string.review_empty_title),
        description = stringResource(R.string.review_empty_description),
        imageContentDescription = stringResource(R.string.review_empty_title),
        modifier = modifier,
    )
}

@Composable
fun CommonEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    imageResId: Int = R.drawable.ic_review_empty,
    imageContentDescription: String = title,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MainThemeFont.TitleSmall,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                modifier = Modifier.size(64.dp),
                painter = painterResource(id = imageResId),
                contentDescription = imageContentDescription,
            )

            if (description != null) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = description,
                    style = MainThemeFont.Body,
                    color = MainThemeColor.Gray5,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommonReviewEmptyStatePreview() {
    PicplzTheme {
        CommonReviewEmptyState()
    }
}
