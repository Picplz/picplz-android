package com.hm.picplz.ui.screen.order_detail.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor.Black
import com.hm.picplz.ui.theme.MainThemeColor.Gray4
import com.hm.picplz.ui.theme.MainThemeFont

@Composable
fun ProductInfoSection(
    photographerImageUrl: String?,
    photographerName: String,
    price: Int,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.order_detail_section_product_info),
            style = MainThemeFont.ButtonDefault,
            color = Black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(102.dp)
                    .border(
                        width = 1.dp,
                        color = Black,
                        shape = RoundedCornerShape(5.dp),
                    ).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AsyncImage(
                model = photographerImageUrl,
                contentDescription = "주문 상품 사진",
                modifier =
                    Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(4.dp)),
            )

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .align(Alignment.Top),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = photographerName,
                    style = MainThemeFont.TitleSmall,
                    color = Black,
                )

                Text(
                    text = stringResource(R.string.price_format, price),
                    style = MainThemeFont.Body,
                    color = Black,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = description,
                    style = MainThemeFont.Caption,
                    color = Gray4,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductInfoSectionPreview() {
    ProductInfoSection(
        photographerImageUrl = "",
        photographerName = "주문고",
        price = 12000,
        description = "작가가 해놓은 한줄소개 기렁지면 이렇게 처리 작가가 해놓은 한줄소개 기렁지면 이렇게 처리",
    )
}
