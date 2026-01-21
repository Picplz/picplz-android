package com.hm.picplz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hm.picplz.navigation.model.SignUpNickname
import com.hm.picplz.navigation.model.SignUpProfile
import com.hm.picplz.navigation.model.SignUpSelectType
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonViewModel
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.SignUpNicknameScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.SignUpProfileImageScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.SignUpSelectTypeScreen

@Composable
fun SignUpCommonNavHost(
    mainNavController: NavHostController,
    signUpCommonNavController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = viewModel(),
) {
    NavHost(
        navController = signUpCommonNavController,
        startDestination = SignUpSelectType,
        modifier = modifier,
    ) {
        composable<SignUpNickname> {
            SignUpNicknameScreen(
                signUpCommonNavController = signUpCommonNavController,
                viewModel = viewModel,
            )
        }
        composable<SignUpProfile> {
            SignUpProfileImageScreen(
                mainNavController = mainNavController,
                signUpCommonNavController = signUpCommonNavController,
                viewModel = viewModel,
            )
        }
        composable<SignUpSelectType> {
            SignUpSelectTypeScreen(
                signUpCommonNavController = signUpCommonNavController,
                mainNavController = mainNavController,
                viewModel = viewModel,
            )
        }
    }
}
