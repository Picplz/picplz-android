package com.hm.picplz.ui.screen.login

import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.R
import com.hm.picplz.navigation.navigateWithBundle
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonHorizontalPager
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.collectLatest

data class LoginIntroPageData(
    val text: String, @DrawableRes val imageRes: Int, val isLast: Boolean = false // 마지막 페이지 여부
)

object LoginIntroPageConfig {
    val PAGES = listOf(
        LoginIntroPageData("내 인생샷 찍어줄\n작가님과 위치기반 매칭!", R.drawable.intro1),
        LoginIntroPageData("작가와 고객 모두 걱정 없는\n정찰제, 안전 결제 시스템!", R.drawable.intro2),
        LoginIntroPageData("나의 인생 프사,\n이젠 픽플즈가 함께", R.drawable.intro3, isLast = true)
    )

    const val IMAGE_HEIGHT_FACTOR = 0.6f
}

val TAG = "LOGININTROSCREEN"

@Composable
fun LoginIntroScreen(
    navController: NavController, viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = screenHeight * LoginIntroPageConfig.IMAGE_HEIGHT_FACTOR

    Scaffold(
        modifier = Modifier.fillMaxSize(), containerColor = MainThemeColor.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            CommonHorizontalPager(
                items = LoginIntroPageConfig.PAGES,
                pageCount = LoginIntroPageConfig.PAGES.size,
                showIndicator = true,
                itemContent = { page ->
                    LoginIntroPage(
                        page, imageHeight
                    ) {
                        viewModel.handleIntent(LoginIntent.NavigateToKaKao)
                    }
                },
                isIndicatorPositionAbsolute = true,
                indicatorTopSpacing = imageHeight + 92.dp + 34.dp
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is LoginSideEffect.NavigateToKaKao -> {
                    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                        if (error != null) {
                            viewModel.handleIntent(LoginIntent.LoginFailed(error))
                        } else if (token != null) {
                            viewModel.handleIntent(LoginIntent.LoginWithKaKao(token.accessToken))
                        }
                    }

                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                            if (token != null) {
                                viewModel.handleIntent(LoginIntent.LoginWithKaKao(token.accessToken))
                            } else if (error != null) {
                                viewModel.handleIntent(LoginIntent.LoginFailed(error))
                            }
                        }
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    }
                }

                is LoginSideEffect.LoginFailed -> {
                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                    Log.e("Kakao", "카카오톡 로그인 실패", sideEffect.error)
                }

                LoginSideEffect.LoginSuccess -> {
                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()

                    // TODO: UserType에 따라 분기점 설정
                    navController.navigate("main") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                is LoginSideEffect.NavigateToSignUp -> {
                    val profileImageUrlBundle = bundleOf(
                        "profileImageUri" to sideEffect.profileImageUrl
                    )
                    navController.navigateWithBundle("sign-up", profileImageUrlBundle)

//                    navController.navigate("sign-up") {
//                        popUpTo(navController.graph.startDestinationId) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
                }
            }
        }
    }
}

@Composable
fun LoginIntroPage(
    page: LoginIntroPageData, imageHeight: Dp, onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = "Background Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        Text(
            text = page.text,
            style = MainFontFamily.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .wrapContentHeight(Alignment.CenterVertically)
        )

        if (page.isLast) {
            Spacer(modifier = Modifier.height(66.dp))

            CommonBottomButton(
                text = "카카오로 계속하기",
                modifier = Modifier.padding(horizontal = 15.dp),
                contentColor = MainThemeColor.Black,
                containerColor = MainThemeColor.Yellow,
                onClick = onButtonClick
            )
            Spacer(modifier = Modifier.height(10.dp))
            CommonBottomButton(
                text = "카카오 로그아웃",
                modifier = Modifier.padding(horizontal = 15.dp),
                contentColor = MainThemeColor.Black,
                containerColor = MainThemeColor.Yellow,
                onClick = {
                    UserApiClient.instance.unlink { error ->
                        if (error != null) {
                            Log.e(TAG, "연결 끊기 실패", error)
                        } else {
                            Log.i(TAG, "연결 끊기 성공 - 동의 항목 UI 다시 표시 가능")
                        }
                    }
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginIntroScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        LoginIntroScreen(navController = navController)
    }
}
