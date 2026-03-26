package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hm.picplz.common.util.CommonUtil
import com.hm.picplz.data.model.PhotographerPortfolio
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.screen.detail_photographer.portfolio.SinglePortfolio
import com.hm.picplz.ui.theme.MainThemeColor
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailPhotographerPortfoliosScreen(
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
    navController: NavController,
    portfolioId: Int,
    photoIndex: Int = 0,
) {
    val currentState = viewModel.state.collectAsState().value
    val portfolios = currentState.portfolios
    val listState = rememberLazyListState()

    // 포트폴리오 ID에 해당하는 index 찾기
    val targetIndex = portfolios.indexOfFirst { it.portfolioId == portfolioId }

    // TopBar 및 LazyColumn 렌더링
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                ) {
                    CommonFixedTopBar(title = "") {
                        viewModel.handleIntent(DetailPhotographerIntent.NavigateToPrev)
                    }

                    PortfolioList(portfolios, portfolioId, photoIndex, listState)
                }
            }
        },
    )

    // 해당 포트폴리오로 스크롤
    LaunchedEffect(targetIndex) {
        if (targetIndex != -1) {
            listState.animateScrollToItem(targetIndex)
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

@Composable
fun PortfolioList(
    portfolios: List<PhotographerPortfolio>,
    portfolioId: Int,
    photoIndex: Int,
    listState: LazyListState,
) {
    LazyColumn(state = listState, modifier = CommonUtil.paddingModifier) {
        items(portfolios) { portfolio ->
            val currentPhotoIndex = if (portfolio.portfolioId == portfolioId) photoIndex else 0
            SinglePortfolio(
                portfolio = portfolio,
                photoIndex = currentPhotoIndex,
            )
        }
    }
}
