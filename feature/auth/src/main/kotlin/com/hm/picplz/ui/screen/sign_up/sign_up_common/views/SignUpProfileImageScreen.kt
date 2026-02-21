package com.hm.picplz.ui.screen.sign_up.sign_up_common.views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.common.model.UserType
import com.hm.picplz.core.ui.R
import com.hm.picplz.navigation.model.SignUpCompletion
import com.hm.picplz.navigation.model.SignUpPhotographer
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.NavigateToSelected
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.SetProfileImageUri
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.ShowFileUploadDialog
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.UploadProfileImage
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonViewModel
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.util.SetStatusBarStyle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpProfileImageScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = viewModel(),
    mainNavController: NavController,
    signUpCommonNavController: NavController,
) {
    SetStatusBarStyle()

    val context = LocalContext.current

    /** 파일 피커 **/
    val filePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri: Uri? ->
            if (uri != null) {
                viewModel.handleIntent(SetProfileImageUri(uri.toString()))
                val imageBytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                if (imageBytes != null) {
                    val filename = uri.lastPathSegment ?: "profile.jpg"
                    viewModel.handleIntent(UploadProfileImage(imageBytes, filename))
                }
            }
        }

    val currentState = viewModel.state.collectAsState().value

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
            CommonTopBar(
                text = "프로필 이미지 업로드",
                onClickBack = { viewModel.handleIntent(NavigateToPrev) },
            )

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
                    Text(
                        text = "안녕하세요 ${currentState.nickname}님!",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Spacer(
                        modifier =
                            Modifier
                                .height(30.dp),
                    )
                    Box(
                        modifier = Modifier.size(160.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        val painter =
                            if (currentState.profileImageUri != null) {
                                rememberAsyncImagePainter(model = currentState.profileImageUri)
                            } else {
                                painterResource(id = R.drawable.default_profile)
                            }
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
                        IconButton(
                            onClick = { viewModel.handleIntent(ShowFileUploadDialog) },
                            modifier =
                                Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(33.dp)
                                    .offset(x = (-5).dp, y = (-5).dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.camera_circle),
                                contentDescription = "이미지 업로드",
                                modifier =
                                    Modifier
                                        .size(33.dp)
                                        .background(Color.Gray, CircleShape),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(70.dp))
                    val guideText =
                        when {
                            currentState.profileImageUri == null -> "프로필 이미지를\n설정해 주세요."
                            currentState.selectedUserType == UserType.Photographer ->
                                "이제 촬영지와 촬영 기기를\n선택할 거예요"
                            else -> null
                        }
                    if (guideText != null) {
                        Text(
                            text = guideText,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
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
                    text =
                        if (currentState.profileImageUri === null) {
                            "다음에 설정하기"
                        } else {
                            "다음"
                        },
                    onClick = { viewModel.handleIntent(NavigateToSelected) },
                    enabled = currentState.nickname.isNotEmpty(),
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
                is SignUpSideEffect.SelectUserTypeScreenSideEffect.NavigateToSelected -> {
                    if (sideEffect.destination == "sign-up-photographer") {
                        mainNavController.navigate(SignUpPhotographer(userInfo = sideEffect.user))
                    } else {
                        mainNavController.navigate(SignUpCompletion(userInfo = sideEffect.user))
                    }
                }
                is SignUpSideEffect.Navigate -> {
                    signUpCommonNavController.navigate(sideEffect.destination)
                }
                is SignUpSideEffect.ShowFileUploadDialog -> {
                    filePickerLauncher.launch("image/*")
                }
            }
        }
    }
}

@Preview
@Composable
fun SignUpProfileImageScreenPreview() {
    val mainNavController = rememberNavController()
    val signUpCommonNavController = rememberNavController()

    PicplzTheme {
        SignUpProfileImageScreen(
            mainNavController = mainNavController,
            signUpCommonNavController = signUpCommonNavController,
        )
    }
}
