package com.hm.picplz.ui.screen.login

import android.util.Log
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonHorizontalPager
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
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
    navController: NavController, viewModel: LoginViewModel = viewModel()
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
                indicatorTopSpacing = imageHeight + 92.dp
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is LoginSideEffect.NavigateToKaKao -> {
                    /**
                     * Todo : 카카오 로그인 관련 로직 추가
                     *  로그인 성공한 경우 -> 로그인 정보를 가지고 메인 페이지로 이동
                     *  로그인 정보가 없는 경우 -> 카카오와 연동된 회원가입 -> 회원가입 정보를 가지고 sign-up 스크린으로 이동
                     */
                    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                        if (error != null) {
                            Log.e(TAG, "카카오계정으로 로그인 실패", error)
                        } else if (token != null) {
                            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                        }
                    }

                    // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                            if (error != null) {
                                Log.e(TAG, "카카오톡으로 로그인 실패", error)

                                // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                                // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                    return@loginWithKakaoTalk
                                }

                                // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                                UserApiClient.instance.loginWithKakaoAccount(
                                    context, callback = callback
                                )
                            } else if (token != null) {
                                requestKakaoLoginInfo(token)
                            }
                        }
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    }


//                    Toast.makeText(context, "카카오 로그인", Toast.LENGTH_LONG).show()
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        navController.navigate("sign-up") {
//                            popUpTo(navController.graph.startDestinationId) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }, 500)
                }

                LoginSideEffect.LoginFailed -> TODO()
                is LoginSideEffect.LoginSuccess -> TODO()
            }
        }
    }
}

private fun requestKakaoLoginInfo(token: OAuthToken) {
    UserApiClient.instance.me { user, error ->
        if (error != null) {
            Log.e(TAG, "사용자 정보 요청 실패", error)
            return@me
        }

        val accessToken = token.accessToken
        val nickname = user?.kakaoAccount?.profile?.nickname
        val profileImageUrl = user?.kakaoAccount?.profile?.thumbnailImageUrl

        Log.i(
            TAG, "사용자 정보 요청 성공" +
                    "\n토큰: ${accessToken}" +
                    "\n닉네임: ${nickname}" +
                    "\n프로필사진: ${profileImageUrl}"
        )
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
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

//        Spacer(modifier = Modifier.height(34.dp))

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
