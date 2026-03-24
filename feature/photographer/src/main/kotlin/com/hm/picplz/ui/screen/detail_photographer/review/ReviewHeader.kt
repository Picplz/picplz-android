package com.hm.picplz.ui.screen.detail_photographer.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.data.model.PhotographerReview
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.util.ReviewUtil
import com.hm.picplz.ui.util.StarType
import com.hm.picplz.feature.photographer.R as PhotographerR

@Composable
fun ReviewHeader(
    modifier: Modifier,
    review: PhotographerReview,
    onReport: () -> Unit,
) {
    val starList = ReviewUtil.calculateStarRating(review.rating, StarType.SUB)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = review.profileImageUri),
            contentDescription =
                stringResource(
                    PhotographerR.string.user_profile,
                ),
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.dp, MainThemeColor.Gray2, CircleShape),
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = review.nickname,
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Black,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                starList.forEach { star ->
                    Image(
                        painter = painterResource(id = star),
                        contentDescription =
                            stringResource(
                                PhotographerR.string.star_rating_desc,
                            ),
                        modifier = Modifier.size(15.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier =
                Modifier
                    .height(17.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MainThemeColor.Gray1)
                    .clickable { onReport() }
                    .padding(horizontal = 4.dp, vertical = 1.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(PhotographerR.string.review_report_title),
                style = ReportButtonTextStyle,
                color = MainThemeColor.Gray3,
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = review.createdAt,
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(PhotographerR.string.written),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
        )
    }
}
