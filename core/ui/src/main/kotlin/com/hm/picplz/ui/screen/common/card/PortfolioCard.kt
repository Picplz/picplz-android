package com.hm.picplz.ui.screen.common.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun PortfolioCard(
    modifier: Modifier = Modifier,
    portfolioImage: Int = R.drawable.ic_launcher_background,
    profileImage: Int = R.drawable.default_profile,
    username: String = "",
    location: String = "",
    bookmarkCnt: Int = 0,
    isBookMarked: Boolean = true,
    borderRadius: Dp = 5.dp,
    bookmarkClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(210.dp)
            .clip(RoundedCornerShape(borderRadius))
            .drawBehind {
                drawRoundRect(
                    color = MainThemeColor.Gray2,
                    style = Stroke(width = 1.dp.toPx()),
                    cornerRadius = CornerRadius(borderRadius.toPx())
                )
            }
            .clickable { onClick() }
    ) {
        Column {
            Image(
                painter = painterResource(id = portfolioImage),
                contentDescription = null,
                modifier = Modifier
                    .size(210.dp)
                    .clip(RoundedCornerShape(borderRadius)),
                contentScale = ContentScale.Crop
            )
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(10.dp)
            ) {
                Image(
                    painter = painterResource(id = profileImage),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$username 작가",
                            style = MainThemeFont.ButtonDefault,
                            color = MainThemeColor.Gray5
                        )
                        Row {
                            Text(text = bookmarkCnt.toString(), style = MainThemeFont.Caption)
                            Spacer(modifier = Modifier.width(5.dp))
                            Image(
                                painter = painterResource(id = if (isBookMarked)
                                    R.drawable.bookmark_active
                                else
                                    R.drawable.bookmark_inactive),
                                contentDescription = "bookmark",
                                modifier = modifier.width(13.dp).clickable { bookmarkClick()}
                            )
                        }

                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.marker_map_gray),
                            contentDescription = "marker",
                            modifier = Modifier.height(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = location,
                            style = MainThemeFont.Caption,
                            color = MainThemeColor.Gray3
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PortfolioCardPreview() {
    PicplzTheme {
        PortfolioCard(
            portfolioImage = R.drawable.logo,
            profileImage = R.drawable.user_undefined,
            username = "유가영",
            location = "무대륙",
            bookmarkCnt = 23,
            isBookMarked = false,
            bookmarkClick = {},
            onClick = {}
        )
    }
}
