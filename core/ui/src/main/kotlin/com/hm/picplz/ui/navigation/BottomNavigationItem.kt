package com.hm.picplz.ui.navigation

import com.hm.picplz.core.ui.R
import com.hm.picplz.navigation.model.Chat as ChatRoute
import com.hm.picplz.navigation.model.Feed as FeedRoute
import com.hm.picplz.navigation.model.Main as MainRoute
import com.hm.picplz.navigation.model.MyPage as MyPageRoute
import com.hm.picplz.navigation.model.Reservation as ReservationRoute
import com.hm.picplz.navigation.model.SearchPhotographer as SearchPhotographerRoute

sealed class BottomNavigationItem(
    val route: Any,
    val label: String,
    val iconSelect: Int,
    val iconUnselect: Int,
) {
    data object Main : BottomNavigationItem(
        MainRoute,
        "홈",
        R.drawable.home_select,
        R.drawable.home_unselect,
    )

    data object Map : BottomNavigationItem(
        SearchPhotographerRoute,
        "지도",
        R.drawable.map_select,
        R.drawable.map_unselect,
    )

    data object Reservation : BottomNavigationItem(
        ReservationRoute,
        "받은 예약",
        R.drawable.reservation_select,
        R.drawable.reservation_unselect,
    )

    data object Feed : BottomNavigationItem(
        FeedRoute,
        "피드",
        R.drawable.feed_select,
        R.drawable.feed_unselect,
    )

    data object Chat : BottomNavigationItem(
        ChatRoute,
        "채팅",
        R.drawable.chat_select,
        R.drawable.chat_unselect,
    )

    data object MyPage : BottomNavigationItem(
        MyPageRoute,
        "마이페이지",
        R.drawable.mypage_select,
        R.drawable.mypage_unselect,
    )
}
