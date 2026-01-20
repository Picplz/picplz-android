package com.hm.picplz.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hm.picplz.navigation.model.*
import com.hm.picplz.ui.screen.chat.ChatScreen
import com.hm.picplz.ui.screen.chat_room.ChatRoomScreen
import com.hm.picplz.ui.screen.feed.FeedScreen
import com.hm.picplz.ui.screen.main.MainScreen
import com.hm.picplz.ui.screen.main.MainSearchScreen
import com.hm.picplz.ui.screen.my_page.MyPageModifyProfileScreen
import com.hm.picplz.ui.screen.my_page.MyPageOrderSheetScreen
import com.hm.picplz.ui.screen.my_page.MyPageScreen
import com.hm.picplz.ui.screen.my_page.MyPageShootingHistoryScreen
import com.hm.picplz.ui.screen.dev.DevScreen
import com.hm.picplz.ui.screen.reservation.ReservationScreen

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    composable<Dev> { DevScreen(navController = navController) }

    composable<Main> { MainScreen(navController = navController) }

    composable<MainSearch> {
        MainSearchScreen(navController = navController)
    }

    composable<Reservation> {
        ReservationScreen(navController = navController)
    }

    composable<Feed> {
        FeedScreen(navController = navController)
    }

    composable<Chat> {
        ChatScreen(navController = navController)
    }

    composable<ChatRoom> { backStackEntry ->
        val args = backStackEntry.toRoute<ChatRoom>()
        ChatRoomScreen(navController = navController, roomId = args.roomId)
    }

    composable<MyPage> {
        MyPageScreen(navController = navController)
    }

    composable<MyPageModifyProfile> {
        MyPageModifyProfileScreen(navController = navController)
    }

    composable<MyPageShootingHistory> {
        MyPageShootingHistoryScreen(navController = navController)
    }

    composable<MyPageOrderSheet> {
        MyPageOrderSheetScreen(navController = navController)
    }
}
