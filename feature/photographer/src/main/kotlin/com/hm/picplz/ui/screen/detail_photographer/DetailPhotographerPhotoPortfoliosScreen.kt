package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hm.picplz.core.ui.R
import com.hm.picplz.data.mockdata.mockPhotographerInfo
import com.hm.picplz.navigation.model.DetailPhotographerPortfolioDetail
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.theme.MainThemeColor

@Suppress("UNUSED_PARAMETER")
@Composable
fun DetailPhotographerPhotoPortfoliosScreen(
    navController: NavController,
    photographerId: Int,
) {
    val photoPortfolios = mockPhotographerInfo.photoPortfolios
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

    val chunkedImages = photoPortfolios.chunked(3)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
            ) {
                CommonFixedTopBar(title = "포트폴리오") {
                    navController.popBackStack()
                }

                Column(
                    modifier =
                        paddingModifier
                            .fillMaxSize()
                            .padding(top = 7.dp, bottom = 20.dp)
                            .verticalScroll(rememberScrollState()),
                ) {
                    chunkedImages.forEach { rowImages ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            rowImages.forEach { imageRes ->
//                                TODO: 기본 로드 이미지 변경
                                Image(
                                    painter =
                                        rememberAsyncImagePainter(
                                            model =
                                                ImageRequest.Builder(
                                                    LocalContext.current,
                                                )
                                                    .data(imageRes.photoPortfolioUri) // 실제 로드할 이미지
                                                    .placeholder(R.drawable.center_char) // 로드되기 전 기본 이미지
                                                    .error(R.drawable.center_char) // 로드 실패 시 보여줄 이미지
                                                    .crossfade(true) // 부드러운 전환 효과
                                                    .build(),
                                        ),
                                    contentDescription = "포트폴리오 이미지",
                                    modifier =
                                        Modifier
                                            .weight(1f) // 각 이미지가 동일한 크기를 가짐
                                            .aspectRatio(1f) // 1:1 비율
                                            .padding(2.dp)
                                            .clickable {
                                                navController.navigate(
                                                    DetailPhotographerPortfolioDetail(
                                                        portfolioId = imageRes.portfolioId,
                                                        photoIndex = imageRes.index,
                                                    ),
                                                )
                                            },
                                    contentScale = ContentScale.Crop,
                                )
                            }
                            // 남은 빈 공간 채우기
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
            }
        },
    )
}
