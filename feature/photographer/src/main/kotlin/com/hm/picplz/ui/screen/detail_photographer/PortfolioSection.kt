package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.common.model.PhotoPortfolio
import com.hm.picplz.core.ui.R
import com.hm.picplz.navigation.model.DetailPhotographerPhotoPortfolios
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.buttonText
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun PortfolioSection(
    modifier: Modifier,
    navController: NavController,
    photoPortfolios: List<PhotoPortfolio>,
    photographerId: Int,
) {
    Column {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = stringResource(R.string.portfolio),
            style = buttonText,
        )

        Spacer(modifier = Modifier.height(7.dp))

        if (photoPortfolios.isEmpty()) {
            Box(
                modifier =
                    modifier
                        .size(100.dp)
                        .background(
                            color = MainThemeColor.Gray1,
                            shape = RoundedCornerShape(8.dp),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.no_portfolio),
                    style = pretendardTypography.bodySmall,
                    color = MainThemeColor.Gray3,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            val chunkedImages = photoPortfolios.take(9).chunked(3)

            Column(
                modifier = modifier.fillMaxWidth(),
            ) {
                chunkedImages.forEach { rowImages ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowImages.forEach { imageRes ->
                            Image(
                                painter =
                                    rememberAsyncImagePainter(
                                        model = imageRes.photoPortfolioUri,
                                    ),
                                contentDescription = stringResource(R.string.portfolio_image),
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        repeat(3 - rowImages.size) {
                            Spacer(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            CommonIconButton(
                label = stringResource(R.string.view_more_portfolio),
                backgroundColor = Color.Transparent,
                textColor = MainThemeColor.Gray4,
                textStyle = MainThemeFont.Caption,
                iconResId = R.drawable.depth_arrow,
                location = "right",
                horizontalPadding = 0.dp,
                verticalPadding = 0.dp,
                gap = 6.dp,
                onClick = {
                    navController.navigate(
                        DetailPhotographerPhotoPortfolios(photographerId),
                    )
                },
                modifier = modifier.align(Alignment.End),
            )
        }
    }
}
