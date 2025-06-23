package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.model.DeviceCategory
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpAddDeviceScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = hiltViewModel(),
    signUpPhotographerNavController: NavController,
    category: DeviceCategory
) {
    val topBarText = when (category) {
        DeviceCategory.PHONE -> "핸드폰 추가"
        DeviceCategory.CAMERA -> "카메라 추가"
    }

    Scaffold(
        modifier = modifier,
    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CommonTopBar(
                text = topBarText,
                onClickBack = {
                    viewModel.handleIntent(SignUpPhotographerIntent.NavigateToPrev)
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    signUpPhotographerNavController.popBackStack()
                }
                else -> {}
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun SignUpAddDeviceScreenPreview() {
    PicplzTheme {
        val signUpPhotographerNavController = rememberNavController()

        SignUpAddDeviceScreen(
            signUpPhotographerNavController = signUpPhotographerNavController,
            category = DeviceCategory.CAMERA
        )
    }
}