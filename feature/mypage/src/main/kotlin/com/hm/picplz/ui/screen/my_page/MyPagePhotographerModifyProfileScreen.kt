package com.hm.picplz.ui.screen.my_page

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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

private object MyPagePhotographerModifyProfileLayoutDefaults {
    val BottomActionAreaHeight = 120.dp
    val HorizontalPadding = 16.dp
    val SectionVerticalPadding = 12.dp
    val LabelToFieldSpacing = 12.dp
    val NicknameBlockBottomSpacing = 12.dp
    val ProfileImageSize = 102.dp
    val CameraButtonTouchTarget = 32.dp
    val CameraIconSize = 26.dp
    val ProfileToFormSpacing = 12.dp
    val SingleLineFieldHeight = 42.dp
    val IntroductionFieldHeight = 136.dp
    val FieldCornerRadius = 5.dp
    val SingleLineFieldVerticalPadding = 10.dp
    val IntroductionFieldTopPadding = 8.dp
    val IntroductionFieldBottomPadding = 18.dp
    val CounterHorizontalPadding = 16.dp
    val CounterBottomPadding = 2.dp
    val IntroductionCounterBottomSpacing = 14.dp
    val IntroductionCounterReservedBottomPadding =
        IntroductionFieldBottomPadding + CounterBottomPadding + IntroductionCounterBottomSpacing
    val FieldHelperTopPadding = 4.dp
}

private val IntroductionCounterTextStyle = MainThemeFont.Caption.copy(fontSize = 11.sp, lineHeight = 14.sp)

@Composable
fun MyPagePhotographerModifyProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MyPagePhotographerModifyProfileViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val photoPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { granted ->
            syncPhotoPermissionState(
                context = context,
                viewModel = viewModel,
                grantedOverride = granted,
            )
        }

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
        syncPhotoPermissionState(
            context = context,
            viewModel = viewModel,
        )
    }

    DisposableEffect(lifecycleOwner, context) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    syncPhotoPermissionState(
                        context = context,
                        viewModel = viewModel,
                    )
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                MyPagePhotographerModifyProfileSideEffect.NavigateBack -> navController.popBackStack()
                MyPagePhotographerModifyProfileSideEffect.RequestPhotoPermission -> {
                    setHasRequestedPhotoPermission(context)
                    photoPermissionLauncher.launch(photoPermission())
                }
                MyPagePhotographerModifyProfileSideEffect.OpenPhotoPermissionSettings -> {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:${context.packageName}"),
                        ),
                    )
                }
                MyPagePhotographerModifyProfileSideEffect.LaunchImagePicker -> {
                    filePickerLauncher.launch("image/*")
                }
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
        modifier =
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        if (state.showPhotoPermissionScreen) {
            MyPagePhotoPermissionScreen(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                actionLabel =
                    stringResource(
                        if (state.shouldOpenPhotoPermissionSettings) {
                            R.string.modify_profile_photo_permission_settings_button
                        } else {
                            R.string.modify_profile_photo_permission_allow_button
                        },
                    ),
                onActionClick = {
                    viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ClickPhotoPermissionAction)
                },
            )
        } else {
            val scrollState = rememberScrollState()

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    PhotographerProfileImageSection(
                        profileImageUri = state.profileImageUri,
                        onChangeImage = {
                            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ClickProfileImage)
                        },
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                MyPagePhotographerModifyProfileLayoutDefaults.ProfileToFormSpacing,
                            ),
                    )

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

                    Spacer(
                        modifier =
                            Modifier.height(
                                MyPagePhotographerModifyProfileLayoutDefaults.SectionVerticalPadding,
                            ),
                    )
                }

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(MyPagePhotographerModifyProfileLayoutDefaults.BottomActionAreaHeight)
                            .padding(horizontal = MyPagePhotographerModifyProfileLayoutDefaults.HorizontalPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CommonBottomButton(
                        text = stringResource(R.string.modify_profile_done),
                        onClick = { viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.Save) },
                        enabled = state.isCompleteEnabled && !state.isLoading,
                    )
                }
            }
        }
    }
}

private fun syncPhotoPermissionState(
    context: Context,
    viewModel: MyPagePhotographerModifyProfileViewModel,
    grantedOverride: Boolean? = null,
) {
    val granted = grantedOverride ?: hasPhotoPermission(context)
    val hasRequested = hasRequestedPhotoPermission(context)
    val permanentlyDenied = !granted && hasRequested && !shouldShowPhotoPermissionRationale(context)

    viewModel.handleIntent(
        MyPagePhotographerModifyProfileIntent.SyncPhotoPermissionState(
            granted = granted,
            hasRequested = hasRequested,
            permanentlyDenied = permanentlyDenied,
        ),
    )
}

private fun photoPermission(): String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

private fun hasPhotoPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(
        context,
        photoPermission(),
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

private fun shouldShowPhotoPermissionRationale(context: Context): Boolean {
    val activity = context.findActivity() ?: return false
    return ActivityCompat.shouldShowRequestPermissionRationale(activity, photoPermission())
}

private fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

private fun hasRequestedPhotoPermission(context: Context): Boolean =
    context.getSharedPreferences(PHOTO_PERMISSION_PREFS_NAME, Context.MODE_PRIVATE)
        .getBoolean(KEY_HAS_REQUESTED_PHOTO_PERMISSION, false)

private fun setHasRequestedPhotoPermission(context: Context) {
    context.getSharedPreferences(PHOTO_PERMISSION_PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(KEY_HAS_REQUESTED_PHOTO_PERMISSION, true)
        .apply()
}

private const val PHOTO_PERMISSION_PREFS_NAME = "mypage_photo_permission_prefs"
private const val KEY_HAS_REQUESTED_PHOTO_PERMISSION = "has_requested_photo_permission"

@Composable
private fun PhotographerProfileImageSection(
    profileImageUri: String,
    onChangeImage: () -> Unit,
) {
    Box(
        modifier = Modifier.size(MyPagePhotographerModifyProfileLayoutDefaults.ProfileImageSize),
        contentAlignment = Alignment.BottomEnd,
    ) {
        if (profileImageUri.isNotEmpty()) {
            AsyncImage(
                model = profileImageUri,
                contentDescription = stringResource(R.string.modify_profile_image),
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(MyPagePhotographerModifyProfileLayoutDefaults.ProfileImageSize)
                        .clip(CircleShape),
            )
        } else {
            Box(
                modifier =
                    Modifier
                        .size(MyPagePhotographerModifyProfileLayoutDefaults.ProfileImageSize)
                        .clip(CircleShape)
                        .background(MainThemeColor.Gray3),
            )
        }

        IconButton(
            onClick = onChangeImage,
            modifier = Modifier.size(MyPagePhotographerModifyProfileLayoutDefaults.CameraButtonTouchTarget),
        ) {
            Image(
                painter = painterResource(id = CoreR.drawable.camera_circle),
                contentDescription = stringResource(R.string.modify_profile_camera),
                modifier = Modifier.size(MyPagePhotographerModifyProfileLayoutDefaults.CameraIconSize),
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
                .padding(
                    horizontal = MyPagePhotographerModifyProfileLayoutDefaults.HorizontalPadding,
                    vertical = MyPagePhotographerModifyProfileLayoutDefaults.SectionVerticalPadding,
                ),
    ) {
        Text(
            text = stringResource(R.string.modify_profile_nickname),
            style = MainThemeFont.TitleSmall,
        )
        Spacer(Modifier.height(MyPagePhotographerModifyProfileLayoutDefaults.LabelToFieldSpacing))
        ModifyProfileOutlinedTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(MyPagePhotographerModifyProfileLayoutDefaults.SingleLineFieldHeight),
            placeholder = stringResource(R.string.modify_profile_nickname_placeholder),
            singleLine = true,
            textStyle = MainThemeFont.Body.copy(color = MainThemeColor.Black),
            isValid = errorMessage == null,
            contentTopPadding = MyPagePhotographerModifyProfileLayoutDefaults.SingleLineFieldVerticalPadding,
            contentBottomPadding = MyPagePhotographerModifyProfileLayoutDefaults.SingleLineFieldVerticalPadding,
        )
        Text(
            text =
                when {
                    errorMessage != null -> errorMessage
                    isCheckingNickname -> stringResource(R.string.modify_profile_nickname_checking)
                    else -> stringResource(R.string.modify_profile_nickname_hint)
                },
            style = MainThemeFont.Caption,
            color = if (errorMessage == null) MainThemeColor.Green120 else MainThemeColor.Red,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = MyPagePhotographerModifyProfileLayoutDefaults.FieldHelperTopPadding),
            textAlign = TextAlign.End,
        )
        Spacer(modifier = Modifier.height(MyPagePhotographerModifyProfileLayoutDefaults.NicknameBlockBottomSpacing))
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
                .padding(
                    horizontal = MyPagePhotographerModifyProfileLayoutDefaults.HorizontalPadding,
                    vertical = MyPagePhotographerModifyProfileLayoutDefaults.SectionVerticalPadding,
                ),
    ) {
        Text(
            text = title,
            style = MainThemeFont.TitleSmall,
        )
        Spacer(Modifier.height(MyPagePhotographerModifyProfileLayoutDefaults.LabelToFieldSpacing))
        ModifyProfileOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(MyPagePhotographerModifyProfileLayoutDefaults.SingleLineFieldHeight),
            placeholder = placeholder,
            singleLine = true,
            textStyle = MainThemeFont.Body.copy(color = MainThemeColor.Black),
            isValid = true,
            contentTopPadding = MyPagePhotographerModifyProfileLayoutDefaults.SingleLineFieldVerticalPadding,
            contentBottomPadding = MyPagePhotographerModifyProfileLayoutDefaults.SingleLineFieldVerticalPadding,
        )
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
                .padding(
                    horizontal = MyPagePhotographerModifyProfileLayoutDefaults.HorizontalPadding,
                    vertical = MyPagePhotographerModifyProfileLayoutDefaults.SectionVerticalPadding,
                ),
    ) {
        Text(
            text = stringResource(R.string.modify_profile_introduction),
            style = MainThemeFont.TitleSmall,
        )
        Spacer(Modifier.height(MyPagePhotographerModifyProfileLayoutDefaults.LabelToFieldSpacing))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(MyPagePhotographerModifyProfileLayoutDefaults.IntroductionFieldHeight),
        ) {
            ModifyProfileOutlinedTextField(
                value = introduction,
                onValueChange = {
                    onIntroductionChange(it.take(INTRODUCTION_MAX_LENGTH))
                },
                modifier = Modifier.fillMaxSize(),
                placeholder = stringResource(R.string.modify_profile_introduction_placeholder),
                textStyle = MainThemeFont.Body.copy(color = MainThemeColor.Black),
                singleLine = false,
                isValid = true,
                contentTopPadding = MyPagePhotographerModifyProfileLayoutDefaults.IntroductionFieldTopPadding,
                contentBottomPadding =
                    MyPagePhotographerModifyProfileLayoutDefaults.IntroductionCounterReservedBottomPadding,
            )
            Column(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = MyPagePhotographerModifyProfileLayoutDefaults.CounterHorizontalPadding),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = stringResource(R.string.modify_profile_introduction_counter, introduction.length),
                    style = IntroductionCounterTextStyle,
                    color = MainThemeColor.Gray3,
                    modifier =
                        Modifier.padding(bottom = MyPagePhotographerModifyProfileLayoutDefaults.CounterBottomPadding),
                )
                Spacer(
                    modifier =
                        Modifier.height(
                            MyPagePhotographerModifyProfileLayoutDefaults.IntroductionCounterBottomSpacing,
                        ),
                )
            }
        }
        if (saveErrorMessage != null) {
            Text(
                text = saveErrorMessage,
                style = MainThemeFont.Caption,
                color = MainThemeColor.Red,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = MyPagePhotographerModifyProfileLayoutDefaults.FieldHelperTopPadding),
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
        focusedTextColor = MainThemeColor.Black,
        unfocusedTextColor = MainThemeColor.Black,
        errorTextColor = MainThemeColor.Black,
        focusedBorderColor = if (isValid) MainThemeColor.Gray3 else MainThemeColor.Red,
        unfocusedBorderColor = if (isValid) MainThemeColor.Gray2 else MainThemeColor.Red,
        errorBorderColor = MainThemeColor.Red,
        focusedPlaceholderColor = MainThemeColor.Gray3,
        unfocusedPlaceholderColor = MainThemeColor.Gray3,
        errorPlaceholderColor = MainThemeColor.Gray3,
        cursorColor = MainThemeColor.Black,
        errorCursorColor = MainThemeColor.Black,
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModifyProfileOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String,
    textStyle: TextStyle,
    singleLine: Boolean,
    isValid: Boolean,
    contentTopPadding: androidx.compose.ui.unit.Dp,
    contentBottomPadding: androidx.compose.ui.unit.Dp,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        interactionSource = interactionSource,
        singleLine = singleLine,
    ) { innerTextField ->
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = singleLine,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            placeholder = {
                Text(
                    text = placeholder,
                    style = textStyle.copy(color = MainThemeColor.Gray3),
                )
            },
            colors = outlinedTextFieldColors(isValid = isValid),
            contentPadding =
                OutlinedTextFieldDefaults.contentPadding(
                    start = MyPagePhotographerModifyProfileLayoutDefaults.HorizontalPadding,
                    top = contentTopPadding,
                    end = MyPagePhotographerModifyProfileLayoutDefaults.HorizontalPadding,
                    bottom = contentBottomPadding,
                ),
            container = {
                OutlinedTextFieldDefaults.Container(
                    enabled = true,
                    isError = !isValid,
                    interactionSource = interactionSource,
                    colors = outlinedTextFieldColors(isValid = isValid),
                    shape = RoundedCornerShape(MyPagePhotographerModifyProfileLayoutDefaults.FieldCornerRadius),
                    unfocusedBorderThickness = 1.dp,
                    focusedBorderThickness = 1.dp,
                )
            },
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun MyPagePhotographerModifyProfileScreenPreview() {
    PicplzTheme {
        MyPagePhotographerModifyProfileScreen(navController = rememberNavController())
    }
}
