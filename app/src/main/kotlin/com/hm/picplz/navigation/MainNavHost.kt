package com.hm.picplz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hm.picplz.navigation.graph.authNavGraph
import com.hm.picplz.navigation.graph.mainNavGraph
import com.hm.picplz.navigation.graph.photographerNavGraph
import com.hm.picplz.navigation.model.Chat
import com.hm.picplz.navigation.model.Dev
import com.hm.picplz.navigation.model.Main
import com.hm.picplz.ui.main.MainActivityUiState

@Composable
fun MainNavHost(
    navController: NavHostController, uiState: MainActivityUiState, modifier: Modifier = Modifier
) {
    // TODO: 개발 완료 후 Main으로 변경
    val startDestination: Any = Dev

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        authNavGraph(navController)
        mainNavGraph(navController)
        photographerNavGraph(navController)
    }
}
