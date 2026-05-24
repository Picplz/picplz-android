package com.hm.picplz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hm.picplz.BuildConfig
import com.hm.picplz.navigation.graph.authNavGraph
import com.hm.picplz.navigation.graph.mainNavGraph
import com.hm.picplz.navigation.graph.photographerNavGraph
import com.hm.picplz.navigation.model.Dev
import com.hm.picplz.navigation.model.Main
import com.hm.picplz.ui.main.MainActivityUiState

@Composable
fun MainNavHost(
    navController: NavHostController,
    @Suppress("UNUSED_PARAMETER") _uiState: MainActivityUiState,
    modifier: Modifier = Modifier,
) {
    val startDestination: Any = if (BuildConfig.DEV_MODE) Dev else Main

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
