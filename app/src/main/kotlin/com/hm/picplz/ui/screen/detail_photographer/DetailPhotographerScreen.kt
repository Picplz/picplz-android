package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.DetailPhotographerViewModel

@Composable
fun DetailPhotographerScreen(
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val currentState = viewModel.state.collectAsState().value
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

    val profileInfo = currentState.profileInfo
    val reviewSummary = currentState.reviewSummary
    val photoPortfolios = profileInfo.photoPortfolios

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        floatingActionButton = {
            Box(modifier = Modifier.padding(start = 30.dp)) {
                CommonBottomButton(
                    text = "예약하기",
                    onClick = { },
                    containerColor = MainThemeColor.Black,
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                CommonFixedTopBar(title = "") {
                    viewModel.handleIntent(DetailPhotographerIntent.NavigateToPrev)
                }

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()) // verticalScroll을 적용
                ) {
                    DetailProfileSection(modifier = paddingModifier, profileInfo = profileInfo)

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        thickness = 10.dp,
                        color = MainThemeColor.Gray2
                    )

                    ReviewSection(
                        modifier = paddingModifier,
                        navController = navController,
                        reviewSummary = reviewSummary
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    PortfolioSection(
                        modifier = paddingModifier,
                        navController = navController,
                        photoPortfolios = photoPortfolios
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    PhotoPriceSection(modifier = paddingModifier)

                    Spacer(modifier = Modifier.height(130.dp))
                }
            }
        })
}

@Preview(showBackground = true)
@Composable
fun DetailPhotographerScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        DetailPhotographerScreen(navController = navController)
    }
}