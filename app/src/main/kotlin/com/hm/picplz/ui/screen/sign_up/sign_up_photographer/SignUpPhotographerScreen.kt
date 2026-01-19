package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.data.model.User
import com.hm.picplz.navigation.SignUpPhotographerNavHost
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerViewModel
import com.hm.picplz.mockdata.emptyUserData

@Composable
fun SignUpPhotographerScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = hiltViewModel(),
    mainNavController: NavHostController,
    userInfo: User = emptyUserData,
) {
    val signUpPhotographerNavController = rememberNavController()

    viewModel.handleIntent(SignUpPhotographerIntent.SetUserInfo(userInfo))

    SignUpPhotographerNavHost(
        mainNavController = mainNavController,
        signUpPhotographerNavController = signUpPhotographerNavController,
        viewModel = viewModel,
        modifier = modifier,
    )
}