package com.hm.picplz.ui.screen.detail_photographer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hm.picplz.R
import com.hm.picplz.data.model.PhotoReview
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.viewmodel.DetailPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun DetailPhotographerPhotoReviewsScreen(
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
    navController: NavController
) {
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

    val photoReviews =
        navController.currentBackStackEntry?.arguments?.getParcelableArrayList(
            "photo-reviews",
            PhotoReview::class.java
        )

    // arguments 접근
//    val images = navController.currentBackStackEntry?.arguments?.getStringArray("photo-reviews")

    val chunkedImages = photoReviews?.chunked(3) ?: emptyList() // 3개씩 나눔, null이면 빈 리스트 반환

    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(MainThemeColor.White) // 배경을 주어 내용이 비치지 않도록
                        .zIndex(1f)
                        .height(56.dp)
                ) {
                    CommonTopBar(
                        text = "사진 리뷰",
                        onClickBack = { viewModel.handleIntent(DetailPhotographerIntent.NavigateToPrev) })
                }

                Column(
                    modifier = paddingModifier
                        .fillMaxSize()
                        .padding(top = 7.dp, bottom = 20.dp)
                        .verticalScroll(rememberScrollState()) // 스크롤 가능하도록 추가
                ) {
                    if (chunkedImages.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.user_undefined),
                                    contentDescription = "user-undefined"
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "등록된 사진 리뷰가 없습니다",
                                    style = pretendardTypography.titleSmall
                                )
                            }
                        }
                    } else {
                        chunkedImages.forEach { rowImages ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                rowImages.forEach { imageRes ->
//                                TODO: 기본 로드 이미지 변경
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = ImageRequest.Builder(
                                                LocalContext.current
                                            )
                                                .data(imageRes.photoReviewUri) // 실제 로드할 이미지
                                                .placeholder(R.drawable.center_char) // 로드되기 전 기본 이미지
                                                .error(R.drawable.center_char) // 로드 실패 시 보여줄 이미지
                                                .crossfade(true) // 부드러운 전환 효과
                                                .build()
                                        ),
                                        contentDescription = "포트폴리오 이미지",
                                        modifier = Modifier
                                            .weight(1f) // 각 이미지가 동일한 크기를 가짐
                                            .aspectRatio(1f) // 1:1 비율
                                            .padding(2.dp)
                                            .clickable {
                                                navController.navigate("detail-photographer-single-review/${imageRes.reviewId}/${imageRes.index}")
                                            },
                                        contentScale = ContentScale.Crop // 이미지 중앙을 기준으로 크기를 맞추고 자름
                                    )
                                }
                                // 남은 빈 공간 채우기
                                repeat(3 - rowImages.size) {
                                    Spacer(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                    )
                                }
                            }
                        }
                    }

                }

            }

            LaunchedEffect(Unit) {
                viewModel.sideEffect.collectLatest { sideEffect ->
                    when (sideEffect) {
                        is DetailPhotographerSideEffect.NavigateToPrev -> {
                            navController.popBackStack()
                        }

                        else -> {}
                    }
                }
            }
        }
    )
}