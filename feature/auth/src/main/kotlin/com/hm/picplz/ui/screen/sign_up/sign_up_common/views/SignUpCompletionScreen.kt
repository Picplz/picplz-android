package com.hm.picplz.ui.screen.sign_up.sign_up_common.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.common.mockdata.emptyUserData
import com.hm.picplz.common.model.User
import com.hm.picplz.common.model.UserType
import com.hm.picplz.core.ui.R
import com.hm.picplz.navigation.model.Main
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonViewModel
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.util.SetStatusBarStyle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpCompletionScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = viewModel(),
    mainNavController: NavController,
    userInfo: User,
) {
    SetStatusBarStyle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier =
                        modifier
                            .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val painter =
                        if (userInfo.profileImageUri != null) {
                            rememberAsyncImagePainter(model = userInfo.profileImageUri)
                        } else {
                            painterResource(id = R.drawable.default_profile_large)
                        }
                    Text(
                        text =
                            buildAnnotatedString {
                                append("안녕하세요 ")
                                append("${userInfo.nickname}님!")
                            },
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(
                        modifier =
                            Modifier
                                .height(27.dp),
                    )
                    Image(
                        painter = painter,
                        contentDescription = "프로필 이미지",
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .size(160.dp)
                                .clip(CircleShape)
                                .background(Color.Gray),
                    )
                    Spacer(
                        modifier =
                            Modifier
                                .height(74.dp),
                    )
                    Text(
                        text =
                            buildAnnotatedString {
                                append("가입을 축하드려요.\n")
                                when (userInfo.userType) {
                                    UserType.Photographer -> append("함께 사진 촬영하러 가볼까요?")
                                    UserType.User -> append("인생샷 건지러 가볼까요")
                                    null -> append("인생샷 건지러 가볼까요")
                                }
                            },
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
            Box(
                modifier =
                    Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center,
            ) {
                CommonBottomButton(
                    text = "픽플즈 시작하기",
                    onClick = {
                        // TODO: 뒤로가기에 대한 처리 필요
                        mainNavController.navigate(Main)
                    },
                    containerColor = MainThemeColor.Black,
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpSideEffect.NavigateToPrev -> {
                    mainNavController.popBackStack()
                }

                is SignUpSideEffect.Navigate -> {
                    mainNavController.navigate(sideEffect.destination)
                }

                else -> {}
            }
        }
    }
}

@Preview
@Composable
fun SignUpCompletionScreenPreview() {
    val mainNavController = rememberNavController()

    PicplzTheme {
        SignUpCompletionScreen(
            mainNavController = mainNavController,
            userInfo = emptyUserData,
        )
    }
}
