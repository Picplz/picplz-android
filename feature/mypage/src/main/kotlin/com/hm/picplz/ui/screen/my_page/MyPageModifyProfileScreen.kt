package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageModifyProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    var nickname by rememberSaveable { mutableStateOf("임두현") }
    var introText by rememberSaveable { mutableStateOf("안녕하세요, 임두현 사진작가입니다.") }
    var instagramId by rememberSaveable { mutableStateOf("@duduhyeon") }
    val introMaxLength = 100

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(title = "프로필 수정") {
                navController.popBackStack()
            }
        },
        floatingActionButton = {
            Box(modifier = Modifier.padding(start = 30.dp)) {
                CommonBottomButton(
                    text = "완료",
                    onClick = { },
                )
            }
        },
        modifier =
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(10.05.dp))

            Box(
                modifier =
                    Modifier
                        .size(102.dp),
                contentAlignment = Alignment.BottomEnd,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "profile-image",
                    modifier =
                        Modifier
                            .size(102.dp)
                            .clip(CircleShape),
                )
                Image(
                    painter = painterResource(id = R.drawable.profile_modify),
                    contentDescription = "profile-modify",
                    modifier =
                        Modifier
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-5).dp, y = (-5).dp),
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 닉네임 입력
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            ) {
                Text(text = "닉네임", style = MainThemeFont.TitleSmall)
                Spacer(Modifier.height(8.dp))

                // 1) 유효성 체크
                val isNicknameValid =
                    remember(nickname) {
                        nickname.matches(Regex("^[가-힣A-Za-z0-9 ]*$"))
                    }

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { input ->
                        nickname = input
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    placeholder = { Text("닉네임을 입력하세요") },
                    singleLine = true,
                    textStyle = MainThemeFont.BodySmaller.copy(color = MainThemeColor.Black),
                    isError = !isNicknameValid,
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MainThemeColor.Gray1,
                            unfocusedContainerColor = MainThemeColor.Gray1,
                            focusedBorderColor = if (isNicknameValid) MainThemeColor.Gray3 else MainThemeColor.Green120,
                            unfocusedBorderColor =
                                if (isNicknameValid) MainThemeColor.Gray2 else MainThemeColor.Green120,
                        ),
                    shape = RoundedCornerShape(5.dp),
                )

                Spacer(Modifier.height(5.dp))

                // 2) 에러일 때만 메시지 표시
                if (!isNicknameValid) {
                    Text(
                        text = "한글, 영문, 숫자만 가능합니다.",
                        style = MainThemeFont.Caption,
                        color = MainThemeColor.Green120,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 자기 소개 입력
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            ) {
                Text(text = "자기 소개", style = MainThemeFont.TitleSmall)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = introText,
                    onValueChange = {
                        if (it.length <= introMaxLength) introText = it
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                    placeholder = { Text("자신을 소개하는 글을 입력해보세요.") },
                    maxLines = 5,
                    textStyle = MainThemeFont.BodySmaller.copy(color = MainThemeColor.Black),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MainThemeColor.Gray1,
                            unfocusedContainerColor = MainThemeColor.Gray1,
                            focusedBorderColor = MainThemeColor.Gray3,
                            unfocusedBorderColor = MainThemeColor.Gray2,
                        ),
                    shape = RoundedCornerShape(5.dp),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${introText.length}/$introMaxLength",
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray4,
                    modifier = Modifier.align(Alignment.End),
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 인스타그램 입력
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            ) {
                Text(text = "인스타그램", style = MainThemeFont.TitleSmall)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = instagramId,
                    onValueChange = { instagramId = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("@username") },
                    singleLine = true,
                    textStyle = MainThemeFont.BodySmaller.copy(color = MainThemeColor.Black),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MainThemeColor.Gray1,
                            unfocusedContainerColor = MainThemeColor.Gray1,
                            focusedBorderColor = MainThemeColor.Gray3,
                            unfocusedBorderColor = MainThemeColor.Gray2,
                        ),
                    shape = RoundedCornerShape(5.dp),
                )
            }
        }
    }
}
