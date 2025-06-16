package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views

import CommonSelectButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.MainActivity
import com.hm.picplz.ui.screen.common.CommonAddButton
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.*
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpDeviceScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = hiltViewModel(),
    signUpPhotographerNavController: NavController,
) {
    /** 상태바 스타일 설정 **/
    val view = LocalView.current
    val activity = LocalContext.current as? MainActivity

    LaunchedEffect(Unit) {
        activity?.window?.apply {
            statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
        }
    }

    val currentState = viewModel.state.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonTopBar(
                text = "활용 기기 선택",
                onClickBack = { viewModel.handleIntent(NavigateToPrev) }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = 16.dp,
                        start = 15.dp,
                        end = 15.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "활용 기기를 선택해 주세요.",
                        style = pretendardTypography.titleMedium
                    )
                    Spacer(
                        modifier = Modifier
                            .height(30.dp)
                    )

                    Column {
                        Text(
                            text = "내 핸드폰",
                            style = pretendardTypography.titleSmall
                        )
                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                        )
                        CommonAddButton (
                            text = "추가하기 +",
                            onClick = {}
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )

                    Column {
                        Text(
                            text = "내 카메라",
                            style = pretendardTypography.titleSmall
                        )
                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                        )
                        CommonAddButton (
                            text = "추가하기 +",
                            onClick = {}
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonBottomButton(
                    text = "다음",
                    onClick = {},
                    containerColor = MainThemeColor.Black
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    signUpPhotographerNavController.popBackStack()
                }
                is SignUpPhotographerSideEffect.Navigate -> {
                    signUpPhotographerNavController.navigate(sideEffect.destination)
                }
                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpDeviceScreenPreview() {
    PicplzTheme {
        val signUpPhotographerNavController = rememberNavController()

        SignUpDeviceScreen(
            signUpPhotographerNavController = signUpPhotographerNavController,
        )
    }
}