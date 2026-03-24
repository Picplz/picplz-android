package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailPhotographerScreen(
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state by viewModel.state.collectAsState()
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        topBar = {
            CommonTopBar(
                text =
                    stringResource(
                        R.string.photographer_name_format,
                        state.profileInfo.name,
                    ),
                onClickBack = {
                    viewModel.handleIntent(DetailPhotographerIntent.NavigateToPrev)
                },
                actions = {
                    IconButton(onClick = { /* TODO: 메뉴 동작 구현 */ }) {
                        Image(
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = stringResource(R.string.menu),
                            modifier = Modifier.size(18.dp),
                        )
                    }
                },
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                CommonBottomButton(
                    text =
                        if (state.profileInfo.isBookable) {
                            stringResource(R.string.booking_button)
                        } else {
                            stringResource(R.string.booking_unavailable)
                        },
                    onClick = { },
                    enabled = state.profileInfo.isBookable,
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
            ) {
                if (state.isBlocked) {
                    BlockedBanner(
                        onUnblock = {
                            viewModel.handleIntent(DetailPhotographerIntent.ToggleBlock)
                        },
                    )
                }

                DetailProfileSection(
                    modifier = paddingModifier,
                    profileInfo = state.profileInfo,
                    isFollow = state.isFollow,
                    isInfoExpanded = state.isInfoExpanded,
                    onToggleFollow = {
                        viewModel.handleIntent(DetailPhotographerIntent.ToggleFollow)
                    },
                    onToggleInfoExpanded = {
                        viewModel.handleIntent(DetailPhotographerIntent.ToggleInfoExpanded)
                    },
                )

                SectionDivider()

                ReviewSection(
                    modifier = paddingModifier,
                    navController = navController,
                    reviewSummary = state.reviewSummary,
                    reviews = state.reviews,
                    photographerId = viewModel.photographerId,
                )

                ThinDivider()

                PortfolioSection(
                    modifier = paddingModifier,
                    navController = navController,
                    photoPortfolios = state.profileInfo.photoPortfolios,
                    photographerId = viewModel.photographerId,
                )

                ThinDivider()

                Spacer(modifier = Modifier.height(20.dp))

                ShootingPackageSection(
                    modifier = paddingModifier,
                    packages = state.shootingPackages,
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        },
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

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
        thickness = 10.dp,
        color = MainThemeColor.Gray1,
    )
}

@Composable
private fun ThinDivider() {
    HorizontalDivider(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
        thickness = 1.dp,
        color = MainThemeColor.Gray1,
    )
}

@Preview(showBackground = true)
@Composable
fun DetailPhotographerScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        DetailPhotographerScreen(navController = navController)
    }
}
