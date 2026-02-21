package com.hm.picplz.ui.screen.sign_up.sign_up_common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.navigation.SignUpCommonNavHost

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = hiltViewModel(),
    mainNavController: NavHostController,
    profileImageUri: String? = null,
    startAt: String? = null,
) {
    val signUpCommonNavController = rememberNavController()

    LaunchedEffect(profileImageUri) {
        profileImageUri?.let { uriString ->
            viewModel.handleIntent(SignUpCommonIntent.SetProfileImageUri(uriString))
        }
    }

    SignUpCommonNavHost(
        mainNavController = mainNavController,
        signUpCommonNavController = signUpCommonNavController,
        viewModel = viewModel,
        modifier = modifier,
        startAt = startAt,
    )
}
