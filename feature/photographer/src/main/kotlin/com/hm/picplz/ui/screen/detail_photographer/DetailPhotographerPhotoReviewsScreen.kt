package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.core.ui.R
import com.hm.picplz.navigation.model.DetailPhotographerSingleReview
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.feature.photographer.R as PhotographerR

@Composable
fun DetailPhotographerPhotoReviewsScreen(
    navController: NavController,
    photographerId: Int,
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val photoReviews = state.reviewSummary.photoReviews
    val chunkedImages = photoReviews.chunked(3)

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
                CommonFixedTopBar(
                    title = stringResource(PhotographerR.string.photo_review_title),
                ) {
                    viewModel.handleIntent(DetailPhotographerIntent.NavigateToPrev)
                }

                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                ) {
                    if (chunkedImages.isEmpty()) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.user_undefined),
                                    contentDescription =
                                        stringResource(
                                            PhotographerR.string.photo_review_empty_icon,
                                        ),
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text =
                                        stringResource(
                                            PhotographerR.string.photo_review_empty,
                                        ),
                                    style = MainThemeFont.TitleSmall,
                                )
                            }
                        }
                    } else {
                        chunkedImages.forEach { rowImages ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                rowImages.forEach { photo ->
                                    Image(
                                        painter =
                                            rememberAsyncImagePainter(
                                                model = photo.photoReviewUri,
                                            ),
                                        contentDescription =
                                            stringResource(
                                                PhotographerR.string.review_photo,
                                            ),
                                        modifier =
                                            Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .padding(1.dp)
                                                .clickable {
                                                    navController.navigate(
                                                        DetailPhotographerSingleReview(
                                                            reviewId = photo.reviewId,
                                                            photoIndex = photo.index,
                                                        ),
                                                    )
                                                },
                                        contentScale = ContentScale.Crop,
                                    )
                                }
                                repeat(3 - rowImages.size) {
                                    Spacer(
                                        modifier =
                                            Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .padding(1.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
    )

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
