package com.hm.picplz.navigation.bottom_navigation

import com.hm.picplz.R

sealed class BottomNavigationItem(
    val route: String,
    val label: String,
    val iconSelect: Int,
    val iconUnselect: Int
) {
    object Main : BottomNavigationItem(
        "main",
        "홈",
        R.drawable.home_select,
        R.drawable.home_unselect,
    )

    object Map : BottomNavigationItem(
        "search-photographer",
        "지도",
        R.drawable.map_select,
        R.drawable.map_unselect,
    )

    object Reservation : BottomNavigationItem(
        "reservation",
        "받은 예약",
        R.drawable.reservation_select,
        R.drawable.reservation_unselect,
    )

    object Feed : BottomNavigationItem(
        "feed",
        "피드",
        R.drawable.feed_select,
        R.drawable.feed_unselect,
    )

    object Chat : BottomNavigationItem(
        "chat",
        "채팅",
        R.drawable.chat_select,
        R.drawable.chat_unselect,
    )

    object MyPage : BottomNavigationItem(
        "mypage",
        "마이페이지",
        R.drawable.mypage_select,
        R.drawable.mypage_unselect,
    )
}