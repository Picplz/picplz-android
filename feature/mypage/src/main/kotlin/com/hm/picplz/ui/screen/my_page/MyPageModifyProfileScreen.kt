package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.common.util.NicknameValidator
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.core.ui.R as CoreR

@Composable
fun MyPageModifyProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    var nickname by rememberSaveable { mutableStateOf("") }

    val nicknameErrors =
        remember(nickname) {
            NicknameValidator.validate(nickname)
        }
    val isNicknameValid = nicknameErrors.isEmpty() || nickname.isEmpty()

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(title = stringResource(R.string.modify_profile_title)) {
                navController.popBackStack()
            }
        },
        floatingActionButton = {
            Box(modifier = Modifier.padding(start = 30.dp)) {
                CommonBottomButton(
                    text = stringResource(R.string.modify_profile_done),
                    onClick = { navController.popBackStack() },
                    enabled = nickname.isNotEmpty() && isNicknameValid,
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
            Spacer(modifier = Modifier.height(10.dp))

            ProfileImageSection()

            Spacer(modifier = Modifier.height(20.dp))

            NicknameSection(
                nickname = nickname,
                onNicknameChange = { nickname = it },
                isValid = isNicknameValid,
                errorMessage = nicknameErrors.firstOrNull()?.message,
            )
        }
    }
}

@Composable
private fun ProfileImageSection() {
    Box(
        modifier = Modifier.size(102.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        Box(
            modifier =
                Modifier
                    .size(102.dp)
                    .clip(CircleShape)
                    .background(MainThemeColor.Gray2),
        )
        Image(
            painter = painterResource(id = CoreR.drawable.camera_circle),
            contentDescription = stringResource(R.string.modify_profile_camera),
            modifier = Modifier.size(27.dp),
        )
    }
}

@Composable
private fun NicknameSection(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    isValid: Boolean,
    errorMessage: String?,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = stringResource(R.string.modify_profile_nickname),
                style = MainThemeFont.TitleSmall,
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = nickname,
                onValueChange = onNicknameChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(stringResource(R.string.modify_profile_nickname_placeholder))
                },
                singleLine = true,
                textStyle = MainThemeFont.Body.copy(color = MainThemeColor.Black),
                isError = !isValid,
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MainThemeColor.Gray1,
                        unfocusedContainerColor = MainThemeColor.Gray1,
                        focusedBorderColor = if (isValid) MainThemeColor.Gray3 else MainThemeColor.Green120,
                        unfocusedBorderColor = if (isValid) MainThemeColor.Gray2 else MainThemeColor.Green120,
                    ),
                shape = RoundedCornerShape(5.dp),
            )
        }
        Text(
            text =
                if (isValid || errorMessage == null) {
                    stringResource(R.string.modify_profile_nickname_hint)
                } else {
                    errorMessage
                },
            style = MainThemeFont.Caption,
            color = if (isValid) MainThemeColor.Green120 else MainThemeColor.Red,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun MyPageModifyProfileScreenPreview() {
    PicplzTheme {
        MyPageModifyProfileScreen(navController = rememberNavController())
    }
}
