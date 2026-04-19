package com.hm.picplz.ui.screen.my_page

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.core.ui.R as CoreR

private const val INTRODUCTION_MAX_LENGTH = 100

@Composable
fun MyPagePhotographerModifyProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MyPagePhotographerModifyProfileViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    val filePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri: Uri? ->
            uri?.toString()?.let {
                viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeProfileImage(it))
                val imageBytes = context.contentResolver.openInputStream(uri)?.use { stream -> stream.readBytes() }
                if (imageBytes != null) {
                    val filename = uri.lastPathSegment ?: "profile.jpg"
                    viewModel.handleIntent(
                        MyPagePhotographerModifyProfileIntent.UploadProfileImage(
                            imageBytes = imageBytes,
                            filename = filename,
                        ),
                    )
                }
            }
        }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                MyPagePhotographerModifyProfileSideEffect.NavigateBack -> navController.popBackStack()
                is MyPagePhotographerModifyProfileSideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.messageResId, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(title = stringResource(R.string.modify_profile_title)) {
                viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.NavigateBack)
            }
        },
        floatingActionButton = {
            Box(modifier = Modifier.padding(start = 30.dp)) {
                CommonBottomButton(
                    text = stringResource(R.string.modify_profile_done),
                    onClick = { viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.Save) },
                    enabled = state.isCompleteEnabled && !state.isLoading,
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

            PhotographerProfileImageSection(
                profileImageUri = state.profileImageUri,
                onChangeImage = { filePickerLauncher.launch("image/*") },
            )

            Spacer(modifier = Modifier.height(20.dp))

            PhotographerNicknameSection(
                nickname = state.nickname,
                isCheckingNickname = state.isCheckingNickname,
                errorMessage = state.representativeNicknameError?.message,
                onNicknameChange = {
                    viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeNickname(it))
                },
            )

            PhotographerTextFieldSection(
                title = stringResource(R.string.modify_profile_instagram_id),
                value = state.instagramId,
                placeholder = stringResource(R.string.modify_profile_instagram_placeholder),
                onValueChange = {
                    viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeInstagramId(it))
                },
            )

            PhotographerIntroductionSection(
                introduction = state.introduction,
                saveErrorMessage = state.saveErrorMessageResId?.let { stringResource(it) },
                onIntroductionChange = {
                    viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeIntroduction(it))
                },
            )
        }
    }
}

@Composable
private fun PhotographerProfileImageSection(
    profileImageUri: String,
    onChangeImage: () -> Unit,
) {
    Box(
        modifier = Modifier.size(102.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        if (profileImageUri.isNotEmpty()) {
            AsyncImage(
                model = profileImageUri,
                contentDescription = stringResource(R.string.modify_profile_image),
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(102.dp)
                        .clip(CircleShape),
            )
        } else {
            Box(
                modifier =
                    Modifier
                        .size(102.dp)
                        .clip(CircleShape)
                        .background(MainThemeColor.Gray2),
            )
        }

        IconButton(
            onClick = onChangeImage,
            modifier = Modifier.size(32.dp),
        ) {
            Image(
                painter = painterResource(id = CoreR.drawable.camera_circle),
                contentDescription = stringResource(R.string.modify_profile_camera),
                modifier = Modifier.size(27.dp),
            )
        }
    }
}

@Composable
private fun PhotographerNicknameSection(
    nickname: String,
    isCheckingNickname: Boolean,
    errorMessage: String?,
    onNicknameChange: (String) -> Unit,
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
                isError = errorMessage != null,
                colors = outlinedTextFieldColors(isValid = errorMessage == null),
                shape = RoundedCornerShape(5.dp),
            )
        }
        Text(
            text =
                when {
                    errorMessage != null -> errorMessage
                    isCheckingNickname -> stringResource(R.string.modify_profile_nickname_checking)
                    else -> stringResource(R.string.modify_profile_nickname_hint)
                },
            style = MainThemeFont.Caption,
            color = if (errorMessage == null) MainThemeColor.Green120 else MainThemeColor.Red,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun PhotographerTextFieldSection(
    title: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = title,
                style = MainThemeFont.TitleSmall,
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(placeholder) },
                singleLine = true,
                textStyle = MainThemeFont.Body.copy(color = MainThemeColor.Black),
                colors = outlinedTextFieldColors(isValid = true),
                shape = RoundedCornerShape(5.dp),
            )
        }
    }
}

@Composable
private fun PhotographerIntroductionSection(
    introduction: String,
    saveErrorMessage: String?,
    onIntroductionChange: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = stringResource(R.string.modify_profile_introduction),
                style = MainThemeFont.TitleSmall,
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = introduction,
                onValueChange = {
                    if (it.length <= INTRODUCTION_MAX_LENGTH) {
                        onIntroductionChange(it)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.modify_profile_introduction_placeholder)) },
                minLines = 5,
                maxLines = 5,
                textStyle = MainThemeFont.Body.copy(color = MainThemeColor.Black),
                colors = outlinedTextFieldColors(isValid = true),
                shape = RoundedCornerShape(5.dp),
            )
        }
        Text(
            text = stringResource(R.string.modify_profile_introduction_counter, introduction.length),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
        )
        if (saveErrorMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = saveErrorMessage,
                style = MainThemeFont.Caption,
                color = MainThemeColor.Red,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun outlinedTextFieldColors(isValid: Boolean) =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MainThemeColor.Gray1,
        unfocusedContainerColor = MainThemeColor.Gray1,
        errorContainerColor = MainThemeColor.Gray1,
        focusedBorderColor = if (isValid) MainThemeColor.Gray3 else MainThemeColor.Red,
        unfocusedBorderColor = if (isValid) MainThemeColor.Gray2 else MainThemeColor.Red,
        errorBorderColor = MainThemeColor.Red,
        cursorColor = MainThemeColor.Black,
        errorCursorColor = MainThemeColor.Black,
    )

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun MyPagePhotographerModifyProfileScreenPreview() {
    PicplzTheme {
        MyPagePhotographerModifyProfileScreen(navController = rememberNavController())
    }
}
