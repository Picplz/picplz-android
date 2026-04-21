package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.common.mockdata.emptyUserData
import com.hm.picplz.common.model.User
import com.hm.picplz.navigation.SignUpPhotographerNavHost

@Composable
fun SignUpPhotographerScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = hiltViewModel(),
    mainNavController: NavHostController,
    userInfo: User? = null,
    startAt: String? = null,
) {
    val signUpPhotographerNavController = rememberNavController()

    LaunchedEffect(userInfo) {
        viewModel.handleIntent(SignUpPhotographerIntent.SetUserInfo(userInfo ?: emptyUserData))
    }

    SignUpPhotographerNavHost(
        mainNavController = mainNavController,
        signUpPhotographerNavController = signUpPhotographerNavController,
        viewModel = viewModel,
        modifier = modifier,
        startAt = startAt,
    )
}
