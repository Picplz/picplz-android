package com.hm.picplz.navigation.bottom_navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hm.picplz.data.model.UserType
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun BottomNavigationBar(navController: NavHostController, userType: UserType = UserType.User) {
    val items = when (userType) {
        UserType.User -> listOf(
            BottomNavigationItem.Main,
            BottomNavigationItem.Map,
            BottomNavigationItem.Feed,
            BottomNavigationItem.Chat,
            BottomNavigationItem.MyPage,
        )

        UserType.Photographer -> listOf(
            BottomNavigationItem.Main,
            BottomNavigationItem.Reservation,
            BottomNavigationItem.Feed,
            BottomNavigationItem.Chat,
            BottomNavigationItem.MyPage,
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier.height(84.dp),
    ) {
        items.forEach { item ->
            AddItem(
                item = item, currentDestination = currentDestination, navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    item: BottomNavigationItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == item.route
    } == true

    BottomNavigationItem(
        selected = isSelected,
        onClick = {}, // 클릭 이벤트 제거(RippleEffect 제거)
        icon = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() }, // RippleEffect 제거
                        indication = null
                    ) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    .size(100.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Image(
                    painter = painterResource(id = if (isSelected) item.iconSelect else item.iconUnselect),
                    contentDescription = "icon",
                    modifier = Modifier.size(30.dp),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.label,
                    style = pretendardTypography.labelMedium,
                    color = if (isSelected) MainThemeColor.Black else MainThemeColor.Gray3
                )
            }
        }
    )
}