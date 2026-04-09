package com.hm.picplz.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hm.picplz.navigation.model.CancelReservationConfirm
import com.hm.picplz.navigation.model.Chat
import com.hm.picplz.navigation.model.ChatRoom
import com.hm.picplz.navigation.model.DetailReservation
import com.hm.picplz.navigation.model.Dev
import com.hm.picplz.navigation.model.Feed
import com.hm.picplz.navigation.model.Main
import com.hm.picplz.navigation.model.MainSearch
import com.hm.picplz.navigation.model.MyPage
import com.hm.picplz.navigation.model.MyPageModifyProfile
import com.hm.picplz.navigation.model.MyPageOrderSheet
import com.hm.picplz.navigation.model.MyPageShootingHistory
import com.hm.picplz.navigation.model.OrderDetail
import com.hm.picplz.navigation.model.Reservation
import com.hm.picplz.ui.screen.cancel_reservation_confirm.CancelReservationConfirmScreen
import com.hm.picplz.ui.screen.chat.ChatScreen
import com.hm.picplz.ui.screen.chat_room.ChatRoomScreen
import com.hm.picplz.ui.screen.detail_reservation.DetailReservationScreen
import com.hm.picplz.ui.screen.dev.DevScreen
import com.hm.picplz.ui.screen.feed.FeedScreen
import com.hm.picplz.ui.screen.main.MainScreen
import com.hm.picplz.ui.screen.main.MainSearchScreen
import com.hm.picplz.ui.screen.my_page.MyPageModifyProfileScreen
import com.hm.picplz.ui.screen.my_page.MyPageOrderSheetScreen
import com.hm.picplz.ui.screen.my_page.MyPageScreen
import com.hm.picplz.ui.screen.my_page.MyPageShootingHistoryScreen
import com.hm.picplz.ui.screen.order_detail.OrderDetailScreen
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
        ChatRoomScreen(navController = navController, _roomId = args.roomId)
    }

    composable<MyPage> {
        MyPageScreen(navController = navController)
    }

    composable<MyPageModifyProfile> {
        MyPageModifyProfileScreen(navController = navController)
    }

    composable<MyPageShootingHistory> { backStackEntry ->
        backStackEntry.toRoute<MyPageShootingHistory>()
        MyPageShootingHistoryScreen(navController = navController)
    }

    composable<MyPageOrderSheet> {
        MyPageOrderSheetScreen(navController = navController)
    }

    composable<DetailReservation> {
        DetailReservationScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateCancelReservation = {
                navController.navigate(CancelReservationConfirm)
            },
            onNavigateToOrderDetail = {
                navController.navigate(OrderDetail)
            },
        )
    }

    composable<CancelReservationConfirm> {
        CancelReservationConfirmScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateHome = {
                navController.navigate(Main) {
                    popUpTo<Dev> { inclusive = false }
                }
            },
        )
    }

    composable<OrderDetail> {
        OrderDetailScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateNextStep = { },
        )
    }
}
