package com.hm.picplz.navigation

import LoginScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.screen.chat.ChatScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPhotoPortfoliosScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPhotoReviewsScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPortfoliosScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerReviewScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerSingleReviewScreen
import com.hm.picplz.ui.screen.feed.FeedScreen
import com.hm.picplz.ui.screen.main.MainScreen
import com.hm.picplz.ui.screen.my_page.MyPageScreen
import com.hm.picplz.ui.screen.my_page.ReservationScreen
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.SignUpCompletionScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerScreen
import com.hm.picplz.viewmodel.MainActivityUiState
import com.hm.picplz.viewmodel.emptyUserData

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainNavHost(
    navController: NavHostController, uiState: MainActivityUiState, modifier: Modifier = Modifier
) {
    val startDestination = when (uiState) {
        is MainActivityUiState.Success -> "main"
        else -> "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable("login") { LoginScreen(navController = navController) }
        composable("main") { MainScreen(navController = navController) }
        composable("sign-up") { SignUpScreen(mainNavController = navController) }
        composable("sign-up-client") { backStackEntry ->
            val userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable("userInfo", User::class.java)
            } else {
                @Suppress("DEPRECATION") backStackEntry.arguments?.getParcelable("userInfo")
            }
            SignUpClientScreen(
                navController = navController, userInfo = userInfo ?: emptyUserData
            )
        }

        composable("sign-up-photographer") { backStackEntry ->
            val userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable("userInfo", User::class.java)
            } else {
                @Suppress("DEPRECATION") backStackEntry.arguments?.getParcelable("userInfo")
            }
            SignUpPhotographerScreen(
                mainNavController = navController, userInfo = userInfo ?: emptyUserData
            )
        }

        composable("sign-up-completion") { backStackEntry ->
            val userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable("userInfo", User::class.java)
            } else {
                @Suppress("DEPRECATION") backStackEntry.arguments?.getParcelable("userInfo")
            }
            SignUpCompletionScreen(
                mainNavController = navController, userInfo = userInfo ?: emptyUserData
            )
        }

        composable("search-photographer") {
            SearchPhotographerScreen(mainNavController = navController)
        }

        composable("main") {
            MainScreen(navController = navController)
        }

        composable("reservation") {
            ReservationScreen(navController = navController)
        }

        composable("feed") {
            FeedScreen(navController = navController)
        }

        composable("chat") {
            ChatScreen(navController = navController)
        }

        composable("mypage") {
            MyPageScreen(navController = navController)
        }

        composable("detail-photographer") {
            DetailPhotographerScreen(navController = navController)
        }

        composable("review-photographer") {
            DetailPhotographerReviewScreen(navController = navController)
        }

        composable("detail-photographer-photo-reviews") {
            DetailPhotographerPhotoReviewsScreen(navController = navController)
        }

        composable(
            route = "detail-photographer-single-review/{reviewId}/{photoIndex}", arguments = listOf(
                navArgument("reviewId") { type = NavType.IntType },
                navArgument("photoIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val reviewId = backStackEntry.arguments?.getInt("reviewId") ?: 0
            val photoIndex = backStackEntry.arguments?.getInt("photoIndex") ?: 0

            DetailPhotographerSingleReviewScreen(
                navController = navController, reviewId = reviewId, photoIndex = photoIndex
            )
        }

        composable("detail-photographer-photo-portfolios") {
            DetailPhotographerPhotoPortfoliosScreen(
                navController = navController,
            )
        }

        composable(
            "detail-photographer-portfolios/{portfolioId}/{photoIndex}", arguments = listOf(
                navArgument("portfolioId") { type = NavType.IntType },
                navArgument("photoIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val portfolioId = backStackEntry.arguments?.getInt("portfolioId") ?: 0
            val photoIndex = backStackEntry.arguments?.getInt("photoIndex") ?: 0

            DetailPhotographerPortfoliosScreen(
                navController = navController, portfolioId = portfolioId, photoIndex = photoIndex
            )
        }
    }
}