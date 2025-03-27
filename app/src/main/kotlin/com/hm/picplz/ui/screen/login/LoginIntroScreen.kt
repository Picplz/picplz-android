package com.hm.picplz.ui.screen.login

import android.os.Handler
import android.os.Looper
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonHorizontalPager
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainTypography
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest

data class LoginIntroPageData(
    val text: String,
    @DrawableRes val imageRes: Int,
    val isLast: Boolean = false // 마지막 페이지 여부
)

object LoginIntroPageConfig {
    val PAGES = listOf(
        LoginIntroPageData("내 인생샷 찍어줄\n픽플과 위치기반 매칭!", R.drawable.logo),
        LoginIntroPageData("인생샷 맛집\n핫플레이스 추천", R.drawable.user_selected),
        LoginIntroPageData("나의 인생 프사,\n이젠 픽플즈가 함께", R.drawable.logo, isLast = true)
    )

    const val IMAGE_HEIGHT_FACTOR = 0.65f
}

@Composable
fun LoginIntroScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = screenHeight * LoginIntroPageConfig.IMAGE_HEIGHT_FACTOR

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
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
                        page,
                        imageHeight
                    ) {
                        viewModel.handleIntent(LoginIntent.NavigateToKaKao)
                    }
                },
                isIndicatorPositionAbsolute = true,
                indicatorTopSpacing = imageHeight + 122.dp
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
                    Toast.makeText(context, "카카오 로그인", Toast.LENGTH_LONG).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate("sign-up") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }, 500)
                }
            }
        }
    }
}

@Composable
fun LoginIntroPage(
    page: LoginIntroPageData,
    imageHeight: Dp,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = page.text,
            style = MainTypography.titleLarge,
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
