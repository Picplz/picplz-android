package com.hm.picplz.ui.screen.detail_photographer.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.core.ui.R
import com.hm.picplz.domain.model.PhotographerReview
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.feature.photographer.R as PhotographerR

@Composable
fun PhotographerInfoSection(
    modifier: Modifier,
    profileImageUri: String,
    photographerName: String,
    photographerId: Int,
    review: PhotographerReview,
    navController: NavController,
) {
    Column(
        modifier = modifier.padding(vertical = 20.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(DetailPhotographer(photographerId))
                    },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = profileImageUri),
                contentDescription =
                    stringResource(
                        PhotographerR.string.photographer_profile,
                    ),
                modifier =
                    Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text =
                    stringResource(
                        PhotographerR.string.photographer_link_format,
                        photographerName,
                    ),
                style = MainThemeFont.ButtonDefault,
                color = MainThemeColor.Gray5,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = R.drawable.depth_arrow),
                contentDescription = stringResource(PhotographerR.string.more),
                modifier = Modifier.size(12.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(
                text = stringResource(PhotographerR.string.option_label),
                style = MainThemeFont.InnerTag,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = review.option,
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(PhotographerR.string.shooting_location),
                style = MainThemeFont.InnerTag,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = review.location,
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter =
                    painterResource(
                        id =
                            if (review.isRecommended) {
                                R.drawable.like_active
                            } else {
                                R.drawable.like_inactive
                            },
                    ),
                contentDescription = stringResource(PhotographerR.string.like),
                modifier =
                    Modifier
                        .size(20.dp)
                        .clickable { },
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = review.recommendationCount.toString(),
                style = RecommendCountStyle,
                color = MainThemeColor.Gray6,
            )
        }
    }
}
