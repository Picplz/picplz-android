package com.hm.picplz.ui.screen.photographer_chat_room.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.feature.chat.R as ChatR

@Composable
fun ReservationInfoBanner(
    customerName: String,
    productName: String,
    customerProfileImageUri: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(customerProfileImageUri ?: R.drawable.active_dot)
                        .crossfade(true)
                        .placeholder(R.drawable.active_dot)
                        .error(R.drawable.active_dot)
                        .build(),
                contentDescription = "고객 프로필",
                modifier =
                    Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MainThemeColor.Gray2),
                contentScale = ContentScale.Crop,
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text =
                        stringResource(
                            id = ChatR.string.photographer_chat_room_reservation_info_label,
                            customerName,
                        ),
                    style = MainFontFamily.caption,
                    color = MainThemeColor.Gray4,
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = productName,
                    style = MainFontFamily.buttonDefault,
                    color = MainThemeColor.Black,
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(id = R.drawable.triangle_right),
                contentDescription = "예약 정보 보기",
                modifier = Modifier.size(12.dp),
                tint = MainThemeColor.Gray4,
            )
        }

        HorizontalDivider(
            color = MainThemeColor.Gray2,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReservationInfoBannerPreview() {
    PicplzTheme {
        ReservationInfoBanner(
            customerName = "애니프사",
            productName = "자연스러운프사",
            customerProfileImageUri = null,
            onClick = {},
        )
    }
}
