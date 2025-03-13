package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.detail_photographer.portfolio.SinglePortfolio
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.utils.CommonUtil
import com.hm.picplz.viewmodel.DetailPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailPhotographerPortfoliosScreen(
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
    navController: NavController,
    portfolioId: Int,
    photoIndex: Int = 0
) {
    val currentState = viewModel.state.collectAsState().value
    val portfolios = currentState.portfolios

    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .background(MainThemeColor.White)
                            .zIndex(1f)
                            .height(56.dp)
                    ) {
                        CommonTopBar(text = "",
                            onClickBack = { viewModel.handleIntent(DetailPhotographerIntent.NavigateToPrev) })
                    }

                    Column(modifier = CommonUtil.paddingModifier) {
                        portfolios.forEach { portfolio ->
                            SinglePortfolio(navController = navController, portfolio = portfolio)
                        }
                    }
                }
            }

        }
    )

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is DetailPhotographerSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
            }
        }
    }
}