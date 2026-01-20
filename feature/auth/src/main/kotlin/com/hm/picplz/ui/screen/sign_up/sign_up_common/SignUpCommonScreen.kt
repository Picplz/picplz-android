package com.hm.picplz.ui.screen.sign_up.sign_up_common

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.navigation.SignUpCommonNavHost
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonViewModel


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = viewModel(),
    mainNavController: NavHostController,
    profileImageUri: String? = null
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
    )
}