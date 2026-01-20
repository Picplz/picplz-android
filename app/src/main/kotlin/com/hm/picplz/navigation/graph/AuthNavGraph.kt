package com.hm.picplz.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hm.picplz.navigation.UserTypeMap
import com.hm.picplz.navigation.model.*
import com.hm.picplz.ui.screen.login.LoginIntroScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.SignUpCompletionScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    composable<Login> { LoginIntroScreen(navController = navController) }

    composable<SignUpIntro> { backStackEntry ->
        val args = backStackEntry.toRoute<SignUpIntro>()
        SignUpScreen(
            mainNavController = navController,
            profileImageUri = args.profileImageUri
        )
    }

    composable<SignUpClient>(typeMap = UserTypeMap) { backStackEntry ->
        val args = backStackEntry.toRoute<SignUpClient>()
        SignUpClientScreen(
            navController = navController,
            userInfo = args.userInfo
        )
    }

    composable<SignUpPhotographer>(typeMap = UserTypeMap) { backStackEntry ->
        val args = backStackEntry.toRoute<SignUpPhotographer>()
        SignUpPhotographerScreen(
            mainNavController = navController,
            userInfo = args.userInfo
        )
    }

    composable<SignUpCompletion>(typeMap = UserTypeMap) { backStackEntry ->
        val args = backStackEntry.toRoute<SignUpCompletion>()
        SignUpCompletionScreen(
            mainNavController = navController,
            userInfo = args.userInfo
        )
    }
}
