package com.hm.picplz.ui.screen.detail_photographer

import CommonChip
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.common.common_chip.CommonIconButton
import com.hm.picplz.ui.screen.detail_photographer.Review.ReviewBars
import com.hm.picplz.ui.screen.detail_photographer.Review.SingleReview
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.NavigateToPrev
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.buttonText
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.utils.ReviewUtil
import com.hm.picplz.utils.StarType
import com.hm.picplz.viewmodel.DetailPhotographerReviewViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailPhotographerReviewScreen(
    viewModel: DetailPhotographerReviewViewModel = hiltViewModel(),
    navController: NavController
) {
    val currentState = viewModel.state.collectAsState().value
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

    val reviewSummary = currentState.reviewSummary
    val reviews = currentState.reviews

    val totalRating = reviewSummary.averageRating
    val starList = ReviewUtil.calculateStarRating(totalRating, StarType.MAIN)

    val images = reviewSummary.photoReviews
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("추천순") }

    val reviewBarItems = reviewSummary.keywordBars

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
                    CommonTopBar(text = "리뷰", onClickBack = { viewModel.handleIntent(DetailPhotographerReviewIntent.NavigateToPrev) })
                }

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()) // verticalScroll을 적용
                ) {
                    Column(modifier = paddingModifier) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "촬영 만족도", style = buttonText)
                            Spacer(modifier = Modifier.width(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                starList.forEach { star ->
                                    Image(
                                        painter = painterResource(id = star),
                                        contentDescription = "별점"
                                    )
                                }
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = totalRating.toString(),
                                    style = pretendardTypography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MainThemeColor.Gray4
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(11.dp))

                        ReviewBars(items = reviewBarItems)

                        Spacer(modifier = Modifier.height(28.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "리뷰", style = buttonText)
                            Spacer(modifier = Modifier.width(4.dp)) // 텍스트 사이에 간격 추가
                            Text(text = reviewSummary.totalReviewCount.toString())
                        }

                        Spacer(modifier = Modifier.height(9.dp))

                        // 리뷰 사진 모아보기
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            images.take(3).forEach { image ->
                                Image(
                                    painter = rememberAsyncImagePainter(model = image),
                                    contentDescription = "리뷰 사진",
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f), // 원하는 크기로 고정
                                    contentScale = ContentScale.Crop // 이미지 중앙을 기준으로 크기를 맞추고 자름
                                )
                            }

                            // 4개 이상일 경우 마지막 사진에 원본 이미지 + 어두운 오버레이 + "+N" 표시
                            if (images.size > 4) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // 원본 이미지
                                    Image(
                                        painter = rememberAsyncImagePainter(model = images[3]), // 네 번째 이미지를 기준으로
                                        contentDescription = "리뷰 사진",
                                        modifier = Modifier
                                            .matchParentSize()
                                            .drawWithContent {
                                                drawContent()
                                                drawRect(
                                                    color = MainThemeColor.Black.copy(alpha = 0.5f), // 어두운 오버레이 효과
                                                    size = size
                                                )
                                            },
                                        contentScale = ContentScale.Crop // 이미지 중앙을 기준으로 크기를 맞추고 자름
                                    )

                                    // "+N" 텍스트 오버레이
                                    Text(
                                        text = "+${images.size - 4}", // 초과된 개수 표시
                                        color = MainThemeColor.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                }
                            } else if (images.size == 4) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = images[3]),
                                    contentDescription = "리뷰 사진",
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    contentScale = ContentScale.Crop // 이미지 중앙을 기준으로 크기를 맞추고 자름
                                )
                            }
                        }
                    }

                    Divider(
                        color = MainThemeColor.Gray2,
                        thickness = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                    )

                    Column(modifier = paddingModifier) {
                        // chip 필터링
                        // TODO: 디자인 적용
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            CommonChip(label = "프로필 Only", isSelected = true, isEditable = false)
                            CommonChip(label = "카카오톡 패키지", isSelected = false, isEditable = false)
                            CommonChip(label = "인스타그램 패키지", isSelected = false, isEditable = false)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 추천순 / 최신순
                        // TODO: 드롭다운 메뉴 컴포넌트화 하기
                        Box {
                            CommonIconButton(label = selectedLabel,
                                backgroundColor = Color.Transparent,
                                textColor = MainThemeColor.Gray5,
                                textStyle = pretendardTypography.bodySmall,
                                iconResId = R.drawable.arrow_down,
                                location = "right",
                                horizontalPadding = 0.dp,
                                verticalPadding = 0.dp,
                                gap = 4.dp,
                                onClick = { expanded = !expanded })

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                offset = DpOffset(0.dp, 5.dp),
                                modifier = Modifier.background(MainThemeColor.White)

                            ) {
                                Column {
                                    DropdownMenuItem(text = { Text("추천순") }, onClick = {
                                        selectedLabel = "추천순" // 선택한 값으로 변경
                                        expanded = false
                                    })
                                    Divider(
                                        color = MainThemeColor.Gray2,
                                        thickness = 1.dp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    DropdownMenuItem(text = { Text("최신순") }, onClick = {
                                        selectedLabel = "최신순" // 선택한 값으로 변경
                                        expanded = false
                                    })
                                }
                            }
                        }

                        // 리스트 형식 (싱글 리뷰)
                        reviews.forEach { item ->
                            SingleReview(item)
                        }
                    }
                }
            }
        })

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when(sideEffect) {
                is DetailPhotographerSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPhotographerReviewScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        DetailPhotographerReviewScreen(navController = navController)
    }
}
