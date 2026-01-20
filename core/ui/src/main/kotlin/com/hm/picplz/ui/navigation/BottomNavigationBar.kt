package com.hm.picplz.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hm.picplz.common.model.UserType
import com.hm.picplz.ui.theme.LocalNavigationHeight
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    userType: UserType = UserType.User,
) {
    val items =
        when (userType) {
            UserType.User ->
                listOf(
                    BottomNavigationItem.Main,
                    BottomNavigationItem.Map,
                    BottomNavigationItem.Feed,
                    BottomNavigationItem.Chat,
                    BottomNavigationItem.MyPage,
                )

            UserType.Photographer ->
                listOf(
                    BottomNavigationItem.Main,
                    BottomNavigationItem.Reservation,
                    BottomNavigationItem.Feed,
                    BottomNavigationItem.Chat,
                    BottomNavigationItem.MyPage,
                )
        }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navBarHeight = 84.dp

    CompositionLocalProvider(
        LocalNavigationHeight provides navBarHeight,
    ) {
        NavigationBar(
            containerColor = MainThemeColor.White,
            contentColor = MainThemeColor.Black,
            modifier =
                Modifier
                    .height(navBarHeight)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(0.dp),
                    ),
        ) {
            items.forEach { item ->
                AddItem(
                    item = item,
                    currentDestination = currentDestination,
                    navController = navController,
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    item: BottomNavigationItem,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    val isSelected = currentDestination?.hasRoute(item.route::class) == true

    NavigationBarItem(
        selected = isSelected,
        onClick = {},
        colors = NavigationBarItemDefaults.colors(indicatorColor = MainThemeColor.Transparent),
        icon = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .size(100.dp),
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Image(
                    painter = painterResource(id = if (isSelected) item.iconSelect else item.iconUnselect),
                    contentDescription = "icon",
                    modifier = Modifier.size(30.dp),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.label,
                    style = pretendardTypography.labelMedium,
                    color = if (isSelected) MainThemeColor.Black else MainThemeColor.Gray3,
                )
            }
        },
    )
}
