package com.hm.picplz.ui.screen.sign_up.sign_up_common

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.navigation.SignUpCommonNavHost
import com.hm.picplz.viewmodel.SignUpCommonViewModel


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = viewModel(),
    mainNavController: NavHostController,
    // TODO: 받아온 카카오 프로필이 이미지 회원가입 시 적용 로직 추가
    profileImageUri: Uri? = null
) {
    val signUpCommonNavController = rememberNavController()

    SignUpCommonNavHost(
        mainNavController = mainNavController,
        signUpCommonNavController = signUpCommonNavController,
        viewModel = viewModel,
        modifier = modifier,
    )
}