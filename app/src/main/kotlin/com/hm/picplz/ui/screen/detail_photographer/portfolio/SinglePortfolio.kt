package com.hm.picplz.ui.screen.detail_photographer.portfolio

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.R
import com.hm.picplz.data.model.PhotographerPortfolio
import com.hm.picplz.ui.screen.detail_photographer.dummyPhotoPortfolio
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.utils.SingleReviewType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SinglePortfolio(
    navController: NavController,
    portfolio: PhotographerPortfolio,
    type: SingleReviewType = SingleReviewType.OVERVIEW,
    photoIndex: Int = 0
) {
    val pagerState = rememberPagerState(pageCount = { portfolio.photoPortfolioCount })

    // 리스트 형식 (싱글 리뷰)
    Column(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
    ) {
        Text(text = portfolio.title)

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.marker_map_gray),
                contentDescription = "marker-map-gray"
            )
            Text(
                text = portfolio.location,
                style = MainFontFamily.caption,
                color = MainThemeColor.Gray3
            )
            Text(text = "|", style = MainFontFamily.caption, color = MainThemeColor.Gray3)
            Text(
                text = portfolio.createdAt,
                style = MainFontFamily.caption,
                color = MainThemeColor.Gray3
            )
        }

        Spacer(modifier = Modifier.height(28.dp))


        Column {
            HorizontalPager(state = pagerState) { page ->
                Image(
                    painter = rememberAsyncImagePainter(model = portfolio.photoPortfolios[page].photoPortfolioUri),
                    contentDescription = "photo-portfolio",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .wrapContentSize()
                        .aspectRatio(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

//                땡땡이 부분
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) MainThemeColor.Black else MainThemeColor.White
                    Box(modifier = Modifier
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(1.dp, MainThemeColor.Black, CircleShape)
                        .size(12.dp)
                        .clickable {})
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }

        HorizontalDivider(
            thickness = 1.dp, color = MainThemeColor.Gray2
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SinglePortfolioPreview() {
    val navController = rememberNavController()
    val dummyPortfolio = PhotographerPortfolio(
        portfolioId = 2,
        title = "서울숲",
        location = "서울 까치산로",
        createdAt = "2024-02-01",
        photoPortfolios = dummyPhotoPortfolio.slice(5..10),
        photoPortfolioCount = 6,
    )

    PicplzTheme {
        SinglePortfolio(navController, dummyPortfolio)
    }
}