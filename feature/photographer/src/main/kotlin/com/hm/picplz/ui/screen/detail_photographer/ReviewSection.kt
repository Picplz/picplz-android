package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hm.picplz.core.ui.R
import com.hm.picplz.data.model.PhotographerReview
import com.hm.picplz.data.model.PhotographerReviewSummary
import com.hm.picplz.navigation.model.ReviewPhotographer
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.screen.detail_photographer.review.SingleReview
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.ui.util.ReviewUtil
import com.hm.picplz.ui.util.StarType

@Composable
fun ReviewSection(
    modifier: Modifier,
    navController: NavHostController,
    reviewSummary: PhotographerReviewSummary,
    reviews: List<PhotographerReview>,
    photographerId: Int,
    onReport: () -> Unit,
) {
    val totalRating = reviewSummary.averageRating
    val starList = ReviewUtil.calculateStarRating(totalRating, StarType.MAIN)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.shooting_satisfaction),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            starList.forEach { star ->
                Image(
                    painter = painterResource(id = star),
                    contentDescription = stringResource(R.string.star_rating),
                )
            }
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = totalRating.toString(),
                style = pretendardTypography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MainThemeColor.Gray4,
            )
        }

        Spacer(modifier = Modifier.height(17.dp))

        if (reviews.isEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.no_reviews),
                style = pretendardTypography.bodyMedium,
                color = MainThemeColor.Gray3,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(20.dp))
        } else {
            Column(modifier = modifier.fillMaxWidth()) {
                SingleReview(
                    navController = navController,
                    review = reviews.first(),
                    onReport = onReport,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            CommonIconButton(
                label =
                    stringResource(
                        R.string.view_all_reviews_format,
                        reviewSummary.totalReviewCount,
                    ),
                backgroundColor = Color.Transparent,
                textColor = MainThemeColor.Gray4,
                textStyle = MainThemeFont.Caption,
                iconResId = R.drawable.depth_arrow,
                location = "right",
                horizontalPadding = 0.dp,
                verticalPadding = 0.dp,
                gap = 6.dp,
                onClick = {
                    navController.navigate(ReviewPhotographer(photographerId))
                },
                modifier =
                    modifier.align(Alignment.End),
            )
        }
    }
}
