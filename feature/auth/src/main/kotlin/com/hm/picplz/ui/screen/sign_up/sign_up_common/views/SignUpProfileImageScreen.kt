package com.hm.picplz.ui.screen.sign_up.sign_up_common.views

import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.common.model.UserType
import com.hm.picplz.feature.auth.R
import com.hm.picplz.navigation.model.SignUpCompletion
import com.hm.picplz.navigation.model.SignUpPhotographer
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.NavigateToSelected
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.ProfileImageReadFailed
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.SetProfileImageUri
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.ShowFileUploadDialog
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.UploadProfileImage
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonViewModel
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.util.SetStatusBarStyle
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.core.ui.R as CoreUiR

@Composable
fun SignUpProfileImageScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = hiltViewModel(),
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
                viewModel.handleIntent(
                    SetProfileImageUri(
                        newProfileImageUri = uri.toString(),
                        isUserSelected = true,
                    ),
                )
                val imageBytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                if (imageBytes != null) {
                    val contentType = context.contentResolver.getType(uri) ?: "image/jpeg"
                    val filename = uri.toProfileImageFilename(contentType)
                    viewModel.handleIntent(UploadProfileImage(imageBytes, filename, contentType))
                } else {
                    viewModel.handleIntent(ProfileImageReadFailed)
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
                text = stringResource(R.string.sign_up_profile_image_top_bar_title),
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
                        text = stringResource(R.string.sign_up_profile_image_greeting, currentState.nickname),
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
                                painterResource(id = CoreUiR.drawable.default_profile)
                            }
                        Image(
                            painter = painter,
                            contentDescription = stringResource(R.string.sign_up_profile_image_content_description),
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
                                painter = painterResource(id = CoreUiR.drawable.camera_circle),
                                contentDescription =
                                    stringResource(R.string.sign_up_profile_image_upload_content_description),
                                modifier =
                                    Modifier
                                        .size(33.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(70.dp))
                    val guideText =
                        when {
                            currentState.profileImageUri == null ->
                                stringResource(R.string.sign_up_profile_image_guide_required)
                            currentState.selectedUserType == UserType.Photographer ->
                                stringResource(R.string.sign_up_profile_image_guide_photographer)
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
                        when {
                            currentState.isUploadingImage -> stringResource(R.string.sign_up_profile_image_uploading)
                            currentState.profileImageUri == null -> stringResource(R.string.sign_up_profile_image_skip)
                            else -> stringResource(R.string.sign_up_next)
                        },
                    onClick = { viewModel.handleIntent(NavigateToSelected) },
                    enabled =
                        currentState.nickname.isNotEmpty() &&
                            !currentState.isUploadingImage &&
                            !currentState.isSubmitting,
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
                is SignUpSideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.messageResId, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}

private fun Uri.toProfileImageFilename(contentType: String): String {
    val rawFilename = lastPathSegment?.substringAfterLast('/')?.takeIf { it.isNotBlank() } ?: "profile"
    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentType) ?: "jpg"
    return if (rawFilename.contains('.')) rawFilename else "$rawFilename.$extension"
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
