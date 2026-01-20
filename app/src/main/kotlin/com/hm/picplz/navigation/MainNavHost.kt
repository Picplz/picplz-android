package com.hm.picplz.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hm.picplz.common.model.User
// Feature: auth
import com.hm.picplz.ui.screen.login.LoginIntroScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.SignUpCompletionScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerScreen
// Feature: chat
import com.hm.picplz.ui.screen.chat.ChatScreen
import com.hm.picplz.ui.screen.chat_room.ChatRoomScreen
// Feature: photographer
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPhotoPortfoliosScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPhotoReviewsScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPortfoliosScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerReviewScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerSingleReviewScreen
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerScreen
// Feature: mypage
import com.hm.picplz.ui.screen.my_page.MyPageModifyProfileScreen
import com.hm.picplz.ui.screen.my_page.MyPageOrderSheetScreen
import com.hm.picplz.ui.screen.my_page.MyPageScreen
import com.hm.picplz.ui.screen.my_page.MyPageShootingHistoryScreen
// Feature: feed
import com.hm.picplz.ui.screen.feed.FeedScreen
// App: not migrated yet
import com.hm.picplz.ui.screen.main.MainScreen
import com.hm.picplz.ui.screen.main.MainSearchScreen
import com.hm.picplz.ui.screen.reservation.ReservationScreen
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainScreen
import com.hm.picplz.ui.screen.photographer_main.composable.EquipmentSettingScreen
import com.hm.picplz.ui.main.MainActivityUiState
import com.hm.picplz.common.mockdata.emptyUserData
import com.hm.picplz.navigation.Routes

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainNavHost(
    navController: NavHostController, uiState: MainActivityUiState, modifier: Modifier = Modifier
) {
    val startDestination = when (uiState) {
        is MainActivityUiState.Success -> Routes.MAIN
        else -> Routes.CHAT
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Routes.LOGIN) { LoginIntroScreen(navController = navController) }
        composable(Routes.MAIN) { MainScreen(navController = navController) }

        composable(Routes.SIGN_UP) { backStackEntry ->
            val profileImageUri: Uri? = backStackEntry.arguments
                ?.getString("profileImageUri")
                ?.let { Uri.parse(it) }
            android.util.Log.d("SignUpViewModel", "받음!!! nickname: ${profileImageUri}")

            SignUpScreen(
                mainNavController = navController,
                profileImageUri = profileImageUri
            )
        }

        composable(Routes.SIGN_UP_CLIENT) { backStackEntry ->
            val userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable("userInfo", User::class.java)
            } else {
                @Suppress("DEPRECATION") backStackEntry.arguments?.getParcelable("userInfo")
            }
            SignUpClientScreen(
                navController = navController, userInfo = userInfo ?: emptyUserData
            )
        }

        composable(Routes.SIGN_UP_PHOTOGRAPHER) { backStackEntry ->
            val userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable("userInfo", User::class.java)
            } else {
                @Suppress("DEPRECATION") backStackEntry.arguments?.getParcelable("userInfo")
            }
            SignUpPhotographerScreen(
                mainNavController = navController, userInfo = userInfo ?: emptyUserData
            )
        }

        composable(Routes.SIGN_UP_COMPLETION) { backStackEntry ->
            val userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable("userInfo", User::class.java)
            } else {
                @Suppress("DEPRECATION") backStackEntry.arguments?.getParcelable("userInfo")
            }
            SignUpCompletionScreen(
                mainNavController = navController, userInfo = userInfo ?: emptyUserData
            )
        }

        composable(Routes.SEARCH_PHOTOGRAPHER) {
            SearchPhotographerScreen(mainNavController = navController)
        }

        composable(Routes.RESERVATION) {
            ReservationScreen(navController = navController)
        }

        composable(Routes.FEED) {
            FeedScreen(navController = navController)
        }

        composable(Routes.CHAT) {
            ChatScreen(navController = navController)
        }

        composable(
            route = Routes.CHAT_ROOM_PATTERN,
            arguments = listOf(
                navArgument("roomId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            ChatRoomScreen(
                navController = navController,
                roomId = roomId ?: ""
            )
        }

        composable(Routes.MY_PAGE) {
            MyPageScreen(navController = navController)
        }

        composable(Routes.PHOTOGRAPHER_MAIN) {
            PhotographerMainScreen(navController = navController)
        }

        composable(Routes.DETAIL_PHOTOGRAPHER) {
            DetailPhotographerScreen(navController = navController)
        }

        composable(Routes.REVIEW_PHOTOGRAPHER) {
            DetailPhotographerReviewScreen(navController = navController)
        }

        composable(Routes.DETAIL_PHOTOGRAPHER_PHOTO_REVIEWS) {
            DetailPhotographerPhotoReviewsScreen(navController = navController)
        }

        composable(
            route = Routes.DETAIL_PHOTOGRAPHER_SINGLE_REVIEW_PATTERN,
            arguments = listOf(
                navArgument("reviewId") { type = NavType.IntType },
                navArgument("photoIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val reviewId = backStackEntry.arguments?.getInt("reviewId") ?: 0
            val photoIndex = backStackEntry.arguments?.getInt("photoIndex") ?: 0

            DetailPhotographerSingleReviewScreen(
                navController = navController, reviewId = reviewId, photoIndex = photoIndex
            )
        }

        composable(Routes.DETAIL_PHOTOGRAPHER_PHOTO_PORTFOLIOS) {
            DetailPhotographerPhotoPortfoliosScreen(
                navController = navController,
            )
        }

        composable(
            route = Routes.DETAIL_PHOTOGRAPHER_PORTFOLIOS_PATTERN,
            arguments = listOf(
                navArgument("portfolioId") { type = NavType.IntType },
                navArgument("photoIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val portfolioId = backStackEntry.arguments?.getInt("portfolioId") ?: 0
            val photoIndex = backStackEntry.arguments?.getInt("photoIndex") ?: 0

            DetailPhotographerPortfoliosScreen(
                navController = navController, portfolioId = portfolioId, photoIndex = photoIndex
            )
        }

        composable(Routes.PHOTOGRAPHER_EQUIPMENT_SETTING) {
            EquipmentSettingScreen(navController = navController)
        }

        composable(Routes.MAIN_SEARCH) {
            MainSearchScreen(navController = navController)
        }

        composable(Routes.MY_PAGE_MODIFY_PROFILE) {
            MyPageModifyProfileScreen(navController = navController)
        }

        composable(Routes.MY_PAGE_SHOOTING_HISTORY) {
            MyPageShootingHistoryScreen(navController = navController)
        }

        composable(Routes.MY_PAGE_ORDER_SHEET) {
            MyPageOrderSheetScreen(navController = navController)
        }
    }
}
