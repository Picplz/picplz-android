package com.hm.picplz.ui.screen.detail_photographer.portfolio

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.R
import com.hm.picplz.data.model.PhotographerPortfolio
import com.hm.picplz.ui.screen.common.CommonDropdownMenu
import com.hm.picplz.ui.screen.common.CommonHorizontalPager
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.screen.common.DropdownMenuItemData
import com.hm.picplz.mockdata.mockPhotoPortfolios
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun SinglePortfolio(
    navController: NavController,
    portfolio: PhotographerPortfolio,
    photoIndex: Int = 0
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = portfolio.title, style = MainFontFamily.titleMediumLarge)

            CommonDropdownMenu(
                triggerButton = {
                    CommonIconButton(
                        iconResId = R.drawable.menu_meatball,
                        backgroundColor = MainThemeColor.Transparent,
                        iconSize = 16.dp
                    )
                }, menuItems = listOf(
                    DropdownMenuItemData("편집하기", MainThemeColor.Gray5),
                    DropdownMenuItemData("대표이미지 수정", MainThemeColor.Gray5),
                    DropdownMenuItemData("삭제하기", MainThemeColor.Red),
                )
            )

        }


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

        CommonHorizontalPager(
            items = portfolio.photoPortfolios,
            pageCount = portfolio.photoPortfolioCount,
            initialPage = photoIndex,
            showIndicator = true
        ) { photo ->
            Image(
                painter = rememberAsyncImagePainter(model = photo.photoPortfolioUri),
                contentDescription = "photo-portfolio",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .wrapContentSize()
                    .aspectRatio(1f)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

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
        photoPortfolios = mockPhotoPortfolios.slice(5..10),
        photoPortfolioCount = 6,
    )

    PicplzTheme {
        SinglePortfolio(navController, dummyPortfolio)
    }
}