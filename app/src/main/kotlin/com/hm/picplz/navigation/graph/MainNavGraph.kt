package com.hm.picplz.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hm.picplz.common.model.CancelConfirmType
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.navigation.model.CancelReservation
import com.hm.picplz.navigation.model.CancelReservationConfirm
import com.hm.picplz.navigation.model.Chat
import com.hm.picplz.navigation.model.ChatRoom
import com.hm.picplz.navigation.model.DetailReservation
import com.hm.picplz.navigation.model.Dev
import com.hm.picplz.navigation.model.Feed
import com.hm.picplz.navigation.model.Main
import com.hm.picplz.navigation.model.MainSearch
import com.hm.picplz.navigation.model.MyPage
import com.hm.picplz.navigation.model.MyPageFollowedPhotographers
import com.hm.picplz.navigation.model.MyPageModifyProfile
import com.hm.picplz.navigation.model.MyPageMyReviews
import com.hm.picplz.navigation.model.MyPageOrderSheet
import com.hm.picplz.navigation.model.MyPagePackageEdit
import com.hm.picplz.navigation.model.MyPagePhotographer
import com.hm.picplz.navigation.model.MyPagePhotographerActiveAreaEdit
import com.hm.picplz.navigation.model.MyPagePhotographerAddDevice
import com.hm.picplz.navigation.model.MyPagePhotographerEquipmentSetting
import com.hm.picplz.navigation.model.MyPagePhotographerKeywordEdit
import com.hm.picplz.navigation.model.MyPagePhotographerModifyProfile
import com.hm.picplz.navigation.model.MyPageShootingHistory
import com.hm.picplz.navigation.model.OrderDetail
import com.hm.picplz.navigation.model.PhotographerChatRoom
import com.hm.picplz.navigation.model.PhotographerDetailReservation
import com.hm.picplz.navigation.model.Reservation
import com.hm.picplz.ui.screen.cancel_reservation.CancelReservationScreen
import com.hm.picplz.ui.screen.cancel_reservation_confirm.CancelReservationConfirmScreen
import com.hm.picplz.ui.screen.chat.ChatScreen
import com.hm.picplz.ui.screen.chat_room.ChatRoomScreen
import com.hm.picplz.ui.screen.detail_reservation.DetailReservationScreen
import com.hm.picplz.ui.screen.dev.DevScreen
import com.hm.picplz.ui.screen.feed.FeedScreen
import com.hm.picplz.ui.screen.main.MainScreen
import com.hm.picplz.ui.screen.main.MainSearchScreen
import com.hm.picplz.ui.screen.my_page.FollowedPhotographersScreen
import com.hm.picplz.ui.screen.my_page.MyPageModifyProfileScreen
import com.hm.picplz.ui.screen.my_page.MyPageOrderSheetScreen
import com.hm.picplz.ui.screen.my_page.MyPagePackageEditRoute
import com.hm.picplz.ui.screen.my_page.MyPagePhotographerActiveAreaEditRoute
import com.hm.picplz.ui.screen.my_page.MyPagePhotographerKeywordEditRoute
import com.hm.picplz.ui.screen.my_page.MyPagePhotographerModifyProfileScreen
import com.hm.picplz.ui.screen.my_page.MyPageScreen
import com.hm.picplz.ui.screen.my_page.MyPageShootingHistoryScreen
import com.hm.picplz.ui.screen.my_page.MyReviewScreen
import com.hm.picplz.ui.screen.order_detail.OrderDetailScreen
import com.hm.picplz.ui.screen.photographer_chat_room.PhotographerChatRoomScreen
import com.hm.picplz.ui.screen.photographer_detail_reservation.PhotographerDetailReservationScreen
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainViewModel
import com.hm.picplz.ui.screen.photographer_main.composable.EquipmentSettingScreen
import com.hm.picplz.ui.screen.photographer_main.composable.PhotographerAddDeviceScreen
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
        ChatRoomScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            _roomId = args.roomId,
        )
    }

    composable<PhotographerChatRoom> { backStackEntry ->
        val args = backStackEntry.toRoute<PhotographerChatRoom>()
        PhotographerChatRoomScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigatePhotographerDetailReservation = {
                navController.navigate(PhotographerDetailReservation)
            },
            _roomId = args.roomId,
        )
    }

    composable<MyPage> {
        MyPageScreen(navController = navController)
    }

    composable<MyPageFollowedPhotographers> {
        FollowedPhotographersScreen(navController = navController)
    }

    composable<MyPagePhotographer> { backStackEntry ->
        val args = backStackEntry.toRoute<MyPagePhotographer>()
        MyPageScreen(
            navController = navController,
            initialHasPhotographerRole = true,
            initialHasShootings = args.hasShootings,
            initialHasPackagePreview = args.hasPackagePreview,
            initialHasPortfolioPreview = args.hasPortfolioPreview,
        )
    }

    composable<MyPageModifyProfile> {
        MyPageModifyProfileScreen(navController = navController)
    }

    composable<MyPagePhotographerModifyProfile> {
        MyPagePhotographerModifyProfileScreen(navController = navController)
    }

    composable<MyPagePhotographerKeywordEdit> { backStackEntry ->
        val args = backStackEntry.toRoute<MyPagePhotographerKeywordEdit>()
        MyPagePhotographerKeywordEditRoute(
            navController = navController,
            photographerId = args.photographerId,
        )
    }

    composable<MyPagePhotographerEquipmentSetting> { backStackEntry ->
        val equipmentViewModel: PhotographerMainViewModel = hiltViewModel(backStackEntry)
        EquipmentSettingScreen(
            viewModel = equipmentViewModel,
            navController = navController,
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateAddDevice = { category ->
                navController.navigate(MyPagePhotographerAddDevice(category = category))
            },
        )
    }

    composable<MyPagePhotographerAddDevice> { backStackEntry ->
        val args = backStackEntry.toRoute<MyPagePhotographerAddDevice>()
        val category =
            when (args.category.lowercase()) {
                "camera" -> DeviceCategory.CAMERA
                else -> DeviceCategory.PHONE
            }
        val equipmentBackStackEntry = navController.getBackStackEntry(MyPagePhotographerEquipmentSetting)
        val equipmentViewModel: PhotographerMainViewModel = hiltViewModel(equipmentBackStackEntry)
        PhotographerAddDeviceScreen(
            category = category,
            navController = navController,
            viewModel = equipmentViewModel,
            onNavigateBack = {
                navController.popBackStack()
            },
        )
    }

    composable<MyPagePackageEdit> { backStackEntry ->
        val args = backStackEntry.toRoute<MyPagePackageEdit>()
        MyPagePackageEditRoute(
            photographerId = args.photographerId,
            onNavigateBack = {
                navController.popBackStack()
            },
        )
    }

    composable<MyPagePhotographerActiveAreaEdit> { backStackEntry ->
        val args = backStackEntry.toRoute<MyPagePhotographerActiveAreaEdit>()
        MyPagePhotographerActiveAreaEditRoute(
            photographerId = args.photographerId,
            onNavigateBack = {
                navController.popBackStack()
            },
        )
    }

    composable<MyPageMyReviews> {
        MyReviewScreen(navController = navController)
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
            onNavigateCancelReservationConfirm = {
                navController.navigate(CancelReservationConfirm(cancelType = CancelConfirmType.WITHOUT_REFUND))
            },
            onNavigateToOrderDetail = { orderId ->
                navController.navigate(OrderDetail(orderId = orderId))
            },
        )
    }

    composable<PhotographerDetailReservation> {
        PhotographerDetailReservationScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateRejectReservationConfirm = {
                // TODO
            },
            onNavigateToOrderDetail = { orderId ->
                navController.navigate(OrderDetail(orderId = orderId))
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

    composable<OrderDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<OrderDetail>()
        OrderDetailScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateNextStep = {
                navController.navigate(CancelReservation(orderId = args.orderId))
            },
        )
    }

    composable<CancelReservation> {
        CancelReservationScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToCancelConfirm = {
                navController.navigate(CancelReservationConfirm(cancelType = CancelConfirmType.WITH_REFUND))
            },
        )
    }
}
