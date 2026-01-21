package com.hm.picplz.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.navigation.model.DetailPhotographerPhotoPortfolios
import com.hm.picplz.navigation.model.DetailPhotographerPhotoReviews
import com.hm.picplz.navigation.model.DetailPhotographerPortfolioDetail
import com.hm.picplz.navigation.model.DetailPhotographerSingleReview
import com.hm.picplz.navigation.model.PhotographerEquipmentSetting
import com.hm.picplz.navigation.model.PhotographerMain
import com.hm.picplz.navigation.model.ReviewPhotographer
import com.hm.picplz.navigation.model.SearchPhotographer
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPhotoPortfoliosScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPhotoReviewsScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPortfoliosScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerReviewScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerSingleReviewScreen
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
        val args = backStackEntry.toRoute<ReviewPhotographer>()
        DetailPhotographerReviewScreen(
            navController = navController,
            photographerId = args.photographerId,
        )
    }

    composable<DetailPhotographerPhotoReviews> { backStackEntry ->
        val args = backStackEntry.toRoute<DetailPhotographerPhotoReviews>()
        DetailPhotographerPhotoReviewsScreen(
            navController = navController,
            photographerId = args.photographerId,
        )
    }

    composable<DetailPhotographerSingleReview> { backStackEntry ->
        val args = backStackEntry.toRoute<DetailPhotographerSingleReview>()
        DetailPhotographerSingleReviewScreen(
            navController = navController,
            reviewId = args.reviewId,
            photoIndex = args.photoIndex,
        )
    }

    composable<DetailPhotographerPhotoPortfolios> { backStackEntry ->
        val args = backStackEntry.toRoute<DetailPhotographerPhotoPortfolios>()
        DetailPhotographerPhotoPortfoliosScreen(
            navController = navController,
            photographerId = args.photographerId,
        )
    }

    composable<DetailPhotographerPortfolioDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<DetailPhotographerPortfolioDetail>()
        DetailPhotographerPortfoliosScreen(
            navController = navController,
            portfolioId = args.portfolioId,
            photoIndex = args.photoIndex,
        )
    }

    composable<PhotographerEquipmentSetting> {
        EquipmentSettingScreen(navController = navController)
    }
}
