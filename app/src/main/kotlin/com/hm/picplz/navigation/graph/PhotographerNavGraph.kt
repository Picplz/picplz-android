package com.hm.picplz.navigation.graph

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hm.picplz.navigation.model.*
import com.hm.picplz.ui.screen.detail_photographer.*
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainScreen
import com.hm.picplz.ui.screen.photographer_main.composable.EquipmentSettingScreen
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerScreen

fun NavGraphBuilder.photographerNavGraph(navController: NavHostController) {
    composable<SearchPhotographer> {
        SearchPhotographerScreen(mainNavController = navController)
    }

    composable<PhotographerMain> {
        PhotographerMainScreen(navController = navController)
    }

    composable<DetailPhotographer> {
        DetailPhotographerScreen(navController = navController)
    }

    composable<ReviewPhotographer> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry<DetailPhotographer>()
        }
        val sharedViewModel: DetailPhotographerViewModel = hiltViewModel(parentEntry)
        DetailPhotographerReviewScreen(
            navController = navController,
            viewModel = sharedViewModel
        )
    }

    composable<DetailPhotographerPhotoReviews> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry<DetailPhotographer>()
        }
        val parentViewModel: DetailPhotographerViewModel = hiltViewModel(parentEntry)
        DetailPhotographerPhotoReviewsScreen(
            navController = navController,
            photoReviews = parentViewModel.state.value.reviewSummary.photoReviews
        )
    }

    composable<DetailPhotographerSingleReview> { backStackEntry ->
        val args = backStackEntry.toRoute<DetailPhotographerSingleReview>()
        DetailPhotographerSingleReviewScreen(
            navController = navController,
            reviewId = args.reviewId,
            photoIndex = args.photoIndex
        )
    }

    composable<DetailPhotographerPhotoPortfolios> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry<DetailPhotographer>()
        }
        val parentViewModel: DetailPhotographerViewModel = hiltViewModel(parentEntry)
        DetailPhotographerPhotoPortfoliosScreen(
            navController = navController,
            photoPortfolios = parentViewModel.state.value.profileInfo.photoPortfolios
        )
    }

    composable<DetailPhotographerPortfolioDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<DetailPhotographerPortfolioDetail>()
        DetailPhotographerPortfoliosScreen(
            navController = navController,
            portfolioId = args.portfolioId,
            photoIndex = args.photoIndex
        )
    }

    composable<PhotographerEquipmentSetting> {
        EquipmentSettingScreen(navController = navController)
    }
}
