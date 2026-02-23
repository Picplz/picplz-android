package com.hm.picplz.ui.screen.sign_up.sign_up_common.views

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.common.util.filterWhitespace
import com.hm.picplz.feature.auth.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonFilledTextField
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonViewModel
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.util.SetStatusBarStyle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpNicknameScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = hiltViewModel(),
    signUpCommonNavController: NavController,
) {
    SetStatusBarStyle()

    val currentState = viewModel.state.collectAsState().value
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .imePadding()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    },
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CommonTopBar(
                text = stringResource(R.string.sign_up_nickname_top_bar_title),
                onClickBack = { viewModel.handleIntent(SignUpCommonIntent.NavigateToPrev) },
            )
            Spacer(modifier = Modifier.height(144.dp))
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus()
                            })
                        },
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(R.string.sign_up_nickname_title),
                    style = MainThemeFont.Title,
                )
                Spacer(modifier = Modifier.height(12.dp))
                CommonFilledTextField(
                    value = currentState.nickname,
                    onValueChange = { newNickname ->
                        viewModel.handleIntent(SignUpCommonIntent.SetNickname(newNickname.filterWhitespace()))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(R.string.sign_up_nickname_placeholder),
                    errors = currentState.nicknameFieldErrors,
                    imeAction = ImeAction.Done,
                    keyboardActions = {
                        focusManager.clearFocus()
                    },
                )
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = stringResource(R.string.sign_up_nickname_guide),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray3,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier =
                    Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center,
            ) {
                CommonBottomButton(
                    text =
                        if (currentState.isCheckingNickname) {
                            stringResource(R.string.sign_up_nickname_checking)
                        } else {
                            stringResource(R.string.sign_up_next)
                        },
                    onClick = { viewModel.handleIntent(SignUpCommonIntent.CheckNicknameDuplicate) },
                    enabled =
                        currentState.nickname.isNotEmpty() &&
                            currentState.nicknameFieldErrors.isEmpty() &&
                            !currentState.isCheckingNickname,
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpSideEffect.NavigateToPrev -> {
                    signUpCommonNavController.popBackStack()
                }
                is SignUpSideEffect.Navigate -> {
                    signUpCommonNavController.navigate(sideEffect.destination)
                }
                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpNicknameScreenPreview() {
    val signUpNavController = rememberNavController()

    PicplzTheme {
        SignUpNicknameScreen(
            signUpCommonNavController = signUpNavController,
        )
    }
}
