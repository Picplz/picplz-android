package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.hm.picplz.common.model.User
import com.hm.picplz.common.model.UserType
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.navigation.model.MyPageFollowedPhotographers
import com.hm.picplz.navigation.model.MyPageModifyProfile
import com.hm.picplz.navigation.model.MyPageMyReviews
import com.hm.picplz.navigation.model.MyPagePackageEdit
import com.hm.picplz.navigation.model.MyPagePhotographerActiveAreaEdit
import com.hm.picplz.navigation.model.MyPagePhotographerEquipmentSetting
import com.hm.picplz.navigation.model.MyPagePhotographerKeywordEdit
import com.hm.picplz.navigation.model.MyPagePhotographerModifyProfile
import com.hm.picplz.navigation.model.MyPageShootingHistory
import com.hm.picplz.navigation.model.SignUpPhotographer
import com.hm.picplz.ui.navigation.BottomNavigationBar
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.screen.my_page.toggleSwitch.ToggleSwitch
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.util.ReviewUtil
import com.hm.picplz.ui.util.StarType
import java.text.NumberFormat
import java.util.Locale
import com.hm.picplz.core.ui.R as CoreR

internal const val KEY_PHOTOGRAPHER_KEYWORD_SUMMARY = "photographer_keyword_summary"

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    initialHasPhotographerRole: Boolean = false,
    initialHasShootings: Boolean = false,
    initialHasPackagePreview: Boolean = false,
    initialHasPortfolioPreview: Boolean = false,
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val keywordSummaryResultFlow =
        remember(savedStateHandle) {
            savedStateHandle?.getStateFlow<String?>(KEY_PHOTOGRAPHER_KEYWORD_SUMMARY, null)
        }
    val keywordSummaryResult by keywordSummaryResultFlow?.collectAsStateWithLifecycle()
        ?: remember { mutableStateOf(null) }
    val context = LocalContext.current
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(keywordSummaryResult) {
        keywordSummaryResult?.let { keywordSummary ->
            viewModel.handleIntent(MyPageIntent.ApplyPhotographerKeywordSummary(keywordSummary))
            savedStateHandle?.remove<String>(KEY_PHOTOGRAPHER_KEYWORD_SUMMARY)
        }
    }

    LaunchedEffect(initialHasPhotographerRole) {
        if (initialHasPhotographerRole && !state.hasPhotographerRole) {
            viewModel.handleIntent(MyPageIntent.ToggleUserMode)
        }
    }

    LaunchedEffect(initialHasShootings, initialHasPackagePreview, initialHasPortfolioPreview) {
        if (initialHasPhotographerRole) {
            viewModel.handleIntent(
                MyPageIntent.ApplyDevPhotographerPreview(
                    hasShootings = initialHasShootings,
                    hasPackagePreview = initialHasPackagePreview,
                    hasPortfolioPreview = initialHasPortfolioPreview,
                ),
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is MyPageSideEffect.NavigateToPhotographerSignUp -> {
                    navController.navigate(SignUpPhotographer(userInfo = state.toPhotographerSignUpUserInfo()))
                }
                is MyPageSideEffect.NavigateToModifyProfile -> {
                    navController.navigate(MyPageModifyProfile)
                }
                is MyPageSideEffect.NavigateToPhotographerModifyProfile -> {
                    navController.navigate(MyPagePhotographerModifyProfile)
                }
                is MyPageSideEffect.NavigateToPhotographerKeywordEdit -> {
                    navController.navigate(
                        MyPagePhotographerKeywordEdit(photographerId = effect.photographerId.toLong()),
                    )
                }
                is MyPageSideEffect.NavigateToPhotographerEquipmentEdit -> {
                    navController.navigate(MyPagePhotographerEquipmentSetting)
                }
                is MyPageSideEffect.NavigateToPackageEdit -> {
                    navController.navigate(MyPagePackageEdit(photographerId = effect.photographerId.toLong()))
                }
                is MyPageSideEffect.NavigateToPhotographerActiveAreaEdit -> {
                    navController.navigate(MyPagePhotographerActiveAreaEdit(effect.photographerId))
                }
                is MyPageSideEffect.NavigateToMyReviews -> {
                    navController.navigate(MyPageMyReviews)
                }
                is MyPageSideEffect.NavigateToFollowedPhotographers -> {
                    navController.navigate(MyPageFollowedPhotographers)
                }
                is MyPageSideEffect.NavigateToShootingHistory -> {
                    navController.navigate(MyPageShootingHistory)
                }
                is MyPageSideEffect.NavigateToPhotographerPreview -> {
                    navController.navigate(
                        DetailPhotographer(
                            photographerId = effect.photographerId,
                            previewMode = true,
                        ),
                    )
                }
                is MyPageSideEffect.NavigateToSettings -> {
                    toastMessage = context.getString(R.string.my_page_settings_pending)
                }
                is MyPageSideEffect.ShowToast -> {
                    toastMessage = context.getString(effect.messageResId)
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        MyPageScreen(
            state = state,
            onIntent = viewModel::handleIntent,
            navController = navController,
            modifier = Modifier.fillMaxSize(),
        )

        toastMessage?.let { message ->
            CommonToast(
                message = message,
                isVisible = true,
                onDismiss = { toastMessage = null },
            )
        }
    }
}

private fun MyPageState.toPhotographerSignUpUserInfo(): User {
    return User(
        id = "0",
        nickname = nickname,
        userType = UserType.User,
        profileImageUri = profileImageUri.ifBlank { null },
    )
}

@Composable
internal fun MyPageScreen(
    state: MyPageState,
    onIntent: (MyPageIntent) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            MyPageTopBar(
                hasPhotographerRole = state.hasPhotographerRole,
                onIntent = onIntent,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                userType = if (state.hasPhotographerRole) UserType.Photographer else UserType.User,
            )
        },
        modifier =
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
        ) {
            if (state.hasPhotographerRole) {
                PhotographerMyPageContent(
                    photographerProfile = state.photographerProfile,
                    onIntent = onIntent,
                )
            } else {
                ProfileRow(
                    nickname = state.nickname,
                    profileImageUri = state.profileImageUri,
                    onModifyProfileClick = { onIntent(MyPageIntent.NavigateToModifyProfile) },
                )

                HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)

                ShootingSection(
                    ongoingShootings = state.ongoingShootings,
                    onHistoryClick = { onIntent(MyPageIntent.NavigateToShootingHistory) },
                )

                HorizontalDivider(thickness = 10.dp, color = MainThemeColor.Gray1)

                MenuSection(onIntent = onIntent)
            }
        }
    }
}

@Composable
private fun MyPageTopBar(
    hasPhotographerRole: Boolean,
    onIntent: (MyPageIntent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.my_page_title),
                style = MainThemeFont.BodyBold,
            )
            Icon(
                painter = painterResource(id = CoreR.drawable.setting),
                contentDescription = stringResource(R.string.my_page_settings),
                modifier =
                    Modifier
                        .size(23.dp)
                        .clickable { onIntent(MyPageIntent.NavigateToSettings) },
            )
        }

        if (hasPhotographerRole) {
            PhotographerBanner(onIntent = onIntent)
        } else {
            BecomePhotographerBanner(onIntent = onIntent)
        }
    }
}

@Composable
private fun PhotographerBanner(onIntent: (MyPageIntent) -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(bannerHeight)
                .background(MainThemeColor.Green120)
                .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.my_page_switch_photographer),
            style = MainThemeFont.BodyBold,
            color = MainThemeColor.White,
        )
        ToggleSwitch(
            checked = true,
            onCheckedChange = { onIntent(MyPageIntent.ToggleUserMode) },
            checkedTrackColor = MainThemeColor.Green100,
            checkedBorderColor = MainThemeColor.Green100,
            checkedInnerTrackColor = MainThemeColor.Green100,
        )
    }
}

@Composable
private fun BecomePhotographerBanner(onIntent: (MyPageIntent) -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(bannerHeight)
                .background(MainThemeColor.Green120)
                .clickable { onIntent(MyPageIntent.NavigateToPhotographerSignUp) }
                .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.my_page_become_photographer),
            style = MainThemeFont.BodyBold,
            color = MainThemeColor.White,
        )
        Icon(
            painter = painterResource(id = CoreR.drawable.triangle_right),
            contentDescription = stringResource(R.string.my_page_become_photographer),
            tint = MainThemeColor.White,
        )
    }
}

@Composable
private fun ProfileRow(
    nickname: String,
    profileImageUri: String,
    onModifyProfileClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (profileImageUri.isNotEmpty()) {
            AsyncImage(
                model = profileImageUri,
                contentDescription = nickname,
                modifier =
                    Modifier
                        .size(36.dp)
                        .border(1.dp, MainThemeColor.Gray2, CircleShape)
                        .clip(CircleShape),
            )
        } else {
            Box(
                modifier =
                    Modifier
                        .size(36.dp)
                        .border(1.dp, MainThemeColor.Gray2, CircleShape)
                        .clip(CircleShape)
                        .background(MainThemeColor.Gray2),
            )
        }

        Text(
            text = nickname.ifEmpty { "-" },
            style = MainThemeFont.BodyBold,
            color = MainThemeColor.Black,
        )

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onModifyProfileClick,
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, MainThemeColor.Gray3),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
        ) {
            Text(
                text = stringResource(R.string.my_page_modify_profile),
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Gray4,
            )
        }
    }
}

@Composable
private fun PhotographerMyPageContent(
    photographerProfile: PhotographerProfile,
    onIntent: (MyPageIntent) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MainThemeColor.Gray1),
    ) {
        PhotographerProfileCard(
            photographerProfile = photographerProfile,
            onModifyProfileClick = { onIntent(MyPageIntent.NavigateToPhotographerModifyProfile) },
            onPreviewClick = { onIntent(MyPageIntent.NavigateToPhotographerPreview) },
            onRegionEditClick = { onIntent(MyPageIntent.NavigateToPhotographerRegionEdit) },
            onKeywordEditClick = { onIntent(MyPageIntent.NavigateToPhotographerKeywordEdit) },
            onEquipmentEditClick = { onIntent(MyPageIntent.NavigateToPhotographerEquipmentEdit) },
            onHistoryClick = { onIntent(MyPageIntent.NavigateToShootingHistory) },
            onSettlementClick = { onIntent(MyPageIntent.NavigateToSettlement) },
        )

        HorizontalDivider(thickness = 10.dp, color = MainThemeColor.Gray1)

        PhotographerDetailsSection(
            packagePreview = photographerProfile.packagePreview,
            portfolioPreviewImageUrls = photographerProfile.portfolioPreviewImageUrls,
            averageRating = photographerProfile.satisfactionSummary.averageRating,
            onPackageEditClick = { onIntent(MyPageIntent.NavigateToPackageEdit) },
            onPortfolioEditClick = { onIntent(MyPageIntent.NavigateToPortfolioEdit) },
        )
    }
}

@Composable
private fun PhotographerDetailsSection(
    packagePreview: PhotographerPackagePreview?,
    portfolioPreviewImageUrls: List<String>,
    averageRating: String,
    onPackageEditClick: () -> Unit,
    onPortfolioEditClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MainThemeColor.White)
                .padding(start = 16.dp, top = 40.dp, end = 16.dp, bottom = 0.dp),
    ) {
        PhotographerPackageSection(
            packagePreview = packagePreview,
            onEditClick = onPackageEditClick,
        )

        PhotographerPortfolioSection(
            previewImageUrls = portfolioPreviewImageUrls,
            onEditClick = onPortfolioEditClick,
        )

        PhotographerSatisfactionSection(averageRating = averageRating)

        Spacer(modifier = Modifier.height(65.dp))
    }
}

@Composable
private fun PhotographerProfileCard(
    photographerProfile: PhotographerProfile,
    onModifyProfileClick: () -> Unit,
    onPreviewClick: () -> Unit,
    onRegionEditClick: () -> Unit,
    onKeywordEditClick: () -> Unit,
    onEquipmentEditClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettlementClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MainThemeColor.White)
                .padding(horizontal = 16.dp, vertical = 20.dp),
    ) {
        val uriHandler = LocalUriHandler.current

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ProfileImage(
                imageUri = photographerProfile.profileImageUri,
                contentDescription = photographerProfile.displayName,
                size = 74.dp,
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = photographerProfile.displayName,
                        style = MainThemeFont.TitleSmall,
                        color = MainThemeColor.Black,
                    )
                    Text(
                        text = stringResource(R.string.my_page_followers_count, photographerProfile.followerCount),
                        style = MainThemeFont.Body,
                        color = MainThemeColor.Gray4,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        painter = painterResource(id = CoreR.drawable.instagram),
                        contentDescription = stringResource(R.string.my_page_instagram_label),
                        tint = Color.Unspecified,
                        modifier = Modifier.size(10.dp),
                    )
                    Text(
                        text = photographerProfile.instagramId,
                        style =
                            MainThemeFont.Caption.copy(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                textDecoration = TextDecoration.Underline,
                            ),
                        color = MainThemeColor.Gray3,
                        modifier =
                            Modifier.clickable(enabled = photographerProfile.isInstagramRegistered) {
                                uriHandler.openUri("https://www.instagram.com/${photographerProfile.instagramId}/")
                            },
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = photographerProfile.introduction,
                    style = MainThemeFont.Body,
                    color = MainThemeColor.Black,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        val canPreviewProfile = photographerProfile.canPreviewProfile

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            PhotographerActionButton(
                text = stringResource(R.string.my_page_modify_profile),
                onClick = onModifyProfileClick,
                modifier = Modifier.weight(1f),
                isPrimary = true,
            )
            PhotographerActionButton(
                text = stringResource(R.string.my_page_preview_profile),
                onClick = onPreviewClick,
                modifier = Modifier.weight(1f),
                isPrimary = false,
                showArrow = true,
                enabled = canPreviewProfile,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        PhotographerInfoRow(
            summary = photographerProfile.regionSummary,
            editText = stringResource(R.string.my_page_region_edit),
            onClick = onRegionEditClick,
        )
        Spacer(modifier = Modifier.height(12.dp))
        PhotographerInfoRow(
            summary = photographerProfile.keywordSummary,
            editText = stringResource(R.string.my_page_keyword_edit),
            onClick = onKeywordEditClick,
        )
        Spacer(modifier = Modifier.height(12.dp))
        PhotographerInfoRow(
            summary = photographerProfile.equipmentSummary,
            editText = stringResource(R.string.my_page_equipment_edit),
            onClick = onEquipmentEditClick,
        )

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            PhotographerShortcutTile(
                iconResId = R.drawable.ic_my_page_shortcut_history,
                label = stringResource(R.string.my_page_photographer_history),
                iconContentDescription = stringResource(R.string.my_page_history_shortcut_icon),
                onClick = onHistoryClick,
                modifier = Modifier.weight(1f),
            )
            PhotographerShortcutTile(
                iconResId = R.drawable.ic_my_page_shortcut_settlement,
                label = stringResource(R.string.my_page_settlement),
                iconContentDescription = stringResource(R.string.my_page_settlement_shortcut_icon),
                onClick = onSettlementClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun PhotographerPackageSection(
    packagePreview: PhotographerPackagePreview?,
    onEditClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        SectionHeader(
            title = stringResource(R.string.my_page_package_title),
            editText = stringResource(R.string.my_page_package_edit),
            onEditClick = onEditClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (packagePreview == null) {
            EmptyPhotographerPackageCard()
            Spacer(modifier = Modifier.height(24.dp))

            CenteredGuideLines(
                lines =
                    listOf(
                        stringResource(R.string.my_page_package_guide_line1),
                        stringResource(R.string.my_page_package_guide_line2),
                    ),
            )
        } else {
            FilledPhotographerPackageCard(packagePreview = packagePreview)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun PhotographerActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean,
    showArrow: Boolean = false,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(5.dp)
    val containerColor = if (isPrimary) MainThemeColor.White else MainThemeColor.Gray2
    val contentColor =
        when {
            isPrimary -> MainThemeColor.Black
            enabled -> MainThemeColor.Gray4
            else -> MainThemeColor.Gray3
        }

    Row(
        modifier =
            modifier
                .height(42.dp)
                .clip(shape)
                .background(containerColor)
                .then(
                    if (isPrimary) {
                        Modifier.border(1.dp, MainThemeColor.Gray3, shape)
                    } else {
                        Modifier
                    },
                )
                .clickable(enabled = enabled, onClick = onClick)
                .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = MainThemeFont.BodyBold,
            color = contentColor,
        )
        if (showArrow) {
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                painter = painterResource(id = CoreR.drawable.depth_arrow),
                contentDescription = stringResource(R.string.my_page_edit_arrow),
                tint = contentColor,
                modifier = Modifier.size(6.dp, 10.dp),
            )
        }
    }
}

@Composable
private fun PhotographerInfoRow(
    summary: String,
    editText: String,
    onClick: () -> Unit,
) {
    val (primaryText, suffixText) = remember(summary) { splitSummaryText(summary) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text =
                buildAnnotatedString {
                    append(primaryText)
                    if (suffixText.isNotEmpty()) {
                        pushStyle(SpanStyle(color = MainThemeColor.Gray4))
                        append(" $suffixText")
                        pop()
                    }
                },
            style = MainThemeFont.Body,
            color = MainThemeColor.Black,
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(12.dp))
        EditToken(
            text = editText,
            onClick = onClick,
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    editText: String? = null,
    onEditClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
        )
        if (onEditClick != null) {
            EditToken(
                text = editText,
                onClick = onEditClick,
            )
        }
    }
}

@Composable
private fun PhotographerShortcutTile(
    iconResId: Int,
    label: String,
    iconContentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .height(74.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MainThemeColor.Black)
                .clickable(onClick = onClick)
                .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = iconContentDescription,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = label,
            style = MainThemeFont.BodyBold,
            color = MainThemeColor.White,
        )
    }
}

@Composable
private fun ProfileImage(
    imageUri: String,
    contentDescription: String,
    size: androidx.compose.ui.unit.Dp,
) {
    if (imageUri.isNotEmpty()) {
        AsyncImage(
            model = imageUri,
            contentDescription = contentDescription,
            modifier =
                Modifier
                    .size(size)
                    .border(1.dp, MainThemeColor.Gray2, CircleShape)
                    .clip(CircleShape),
        )
    } else {
        Box(
            modifier =
                Modifier
                    .size(size)
                    .border(1.dp, MainThemeColor.Gray2, CircleShape)
                    .clip(CircleShape)
                    .background(MainThemeColor.Gray2),
        )
    }
}

@Composable
private fun ShootingSection(
    ongoingShootings: List<OngoingShootingItem>,
    onHistoryClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 40.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text =
                    if (ongoingShootings.isEmpty()) {
                        stringResource(R.string.my_page_ongoing_shooting)
                    } else {
                        stringResource(R.string.my_page_ongoing_shooting_count, ongoingShootings.size)
                    },
                style = MainThemeFont.TitleSmall,
            )
            Row(
                modifier = Modifier.clickable { onHistoryClick() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.my_page_shooting_history),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray3,
                )
                Box(
                    modifier = Modifier.size(20.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = CoreR.drawable.depth_arrow),
                        contentDescription = stringResource(R.string.my_page_shooting_history),
                        tint = MainThemeColor.Gray3,
                        modifier = Modifier.size(5.dp, 10.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (ongoingShootings.isEmpty()) {
            EmptyShootingCard(modifier = Modifier.padding(horizontal = 16.dp))
        } else {
            val cardWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp
            val listState = rememberLazyListState()
            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            ) {
                items(ongoingShootings.size) { index ->
                    ShootingCard(
                        item = ongoingShootings[index],
                        modifier = Modifier.width(cardWidth),
                    )
                }
            }
        }
    }
}

@Composable
private fun CenteredGuideLines(lines: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        lines.forEach { line ->
            Text(
                text = line,
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Gray6,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun PhotographerPortfolioSection(
    previewImageUrls: List<String>,
    onEditClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        SectionHeader(
            title = stringResource(R.string.my_page_portfolio_title),
            editText = stringResource(R.string.my_page_portfolio_edit),
            onEditClick = onEditClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (previewImageUrls.isEmpty()) {
            EmptyPhotographerPortfolioCard()
        } else {
            FilledPhotographerPortfolioRow(previewImageUrls = previewImageUrls)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun FilledPhotographerPortfolioRow(previewImageUrls: List<String>) {
    val chunkedImages = previewImageUrls.chunked(3)

    Column(modifier = Modifier.fillMaxWidth()) {
        chunkedImages.forEach { rowImages ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowImages.forEach { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.my_page_portfolio_title),
                        modifier =
                            Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clip(RoundedCornerShape(0.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }

                repeat(3 - rowImages.size) {
                    Spacer(
                        modifier =
                            Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotographerSatisfactionSection(averageRating: String) {
    val rating = averageRating.toFloatOrNull() ?: 0f
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.my_page_satisfaction_title),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            ReviewUtil.calculateStarRating(rating, StarType.MAIN).forEach { starResId ->
                Image(
                    painter = painterResource(id = starResId),
                    contentDescription = stringResource(CoreR.string.star_rating),
                    modifier = Modifier.size(20.dp),
                )
            }

            Text(
                text = averageRating,
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}

@Composable
private fun EditToken(
    text: String? = null,
    onClick: () -> Unit,
) {
    Text(
        text = text ?: stringResource(R.string.my_page_edit),
        style = MainThemeFont.InnerTag,
        color = MainThemeColor.Green120,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@Composable
private fun EmptyPhotographerPackageCard() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MainThemeColor.Gray1),
        contentAlignment = Alignment.TopStart,
    ) {
        EmptyStateLines(
            lines =
                listOf(
                    stringResource(R.string.my_page_empty_package_line1),
                    stringResource(R.string.my_page_empty_package_line2),
                    stringResource(R.string.my_page_empty_package_line3),
                ),
            modifier = Modifier.padding(start = 22.dp, top = 19.dp),
        )
    }
}

@Composable
private fun EmptyPhotographerPortfolioCard() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MainThemeColor.Gray1),
        contentAlignment = Alignment.TopStart,
    ) {
        EmptyStateLines(
            lines =
                listOf(
                    stringResource(R.string.my_page_empty_portfolio_line1),
                    stringResource(R.string.my_page_empty_portfolio_line2),
                    stringResource(R.string.my_page_empty_portfolio_line3),
                ),
            modifier = Modifier.padding(start = 22.dp, top = 19.dp),
        )
    }
}

@Composable
private fun EmptyStateLines(
    lines: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        lines.forEach { line ->
            Text(
                text = line,
                style = MainThemeFont.BodyLarge.copy(lineHeight = 22.4.sp),
                color = MainThemeColor.Gray4,
            )
        }
    }
}

@Composable
private fun FilledPhotographerPackageCard(packagePreview: PhotographerPackagePreview) {
    val formattedPrice =
        remember(packagePreview.price) {
            NumberFormat.getNumberInstance(Locale.KOREA).format(packagePreview.price)
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(MainThemeColor.White),
    ) {
        AsyncImage(
            model = packagePreview.imageUrl,
            contentDescription = packagePreview.title,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(160.dp),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 19.dp),
        ) {
            Text(
                text = packagePreview.title,
                style = MainThemeFont.Title,
                color = MainThemeColor.Black,
            )
            Text(
                text = stringResource(R.string.order_detail_price_won_format, formattedPrice),
                style = MainThemeFont.Title,
                color = MainThemeColor.Black,
            )
            Spacer(modifier = Modifier.height(16.dp))
            PackageMetaRow(
                label = stringResource(R.string.my_page_package_meta_time),
                value = packagePreview.meta,
            )
            Spacer(modifier = Modifier.height(10.dp))
            PackageMetaRow(
                label = stringResource(R.string.my_page_package_meta_guide),
                value = packagePreview.description,
                singleLine = false,
            )
        }
    }
}

@Composable
private fun PackageMetaRow(
    label: String,
    value: String,
    singleLine: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
    ) {
        Text(
            text = label,
            style = MainThemeFont.InnerTag,
            color = MainThemeColor.Gray6,
        )
        Text(
            text = value,
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
            maxLines = if (singleLine) 1 else Int.MAX_VALUE,
            modifier = Modifier.weight(1f),
        )
    }
}

private fun splitSummaryText(summary: String): Pair<String, String> {
    val suffixStartIndex = summary.indexOf(" 외 ")

    return if (suffixStartIndex >= 0) {
        val primaryText = summary.substring(0, suffixStartIndex).trim()
        val suffixText = summary.substring(suffixStartIndex + 1).trim()
        primaryText to suffixText
    } else {
        summary to ""
    }
}

@Composable
private fun EmptyShootingCard(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .border(1.dp, MainThemeColor.Gray3, RoundedCornerShape(5.dp))
                .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.my_page_no_ongoing_shooting),
                style = MainThemeFont.ButtonDefault,
                color = MainThemeColor.Black,
            )
            Text(
                text = stringResource(R.string.my_page_no_ongoing_shooting_sub),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
            )
        }
        Icon(
            painter = painterResource(id = CoreR.drawable.triangle_right),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MainThemeColor.Black,
        )
    }
}

@Composable
private fun ShootingCard(
    item: OngoingShootingItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(MainThemeColor.Gray1, RoundedCornerShape(5.dp))
                .border(1.dp, MainThemeColor.Gray2, RoundedCornerShape(5.dp))
                .padding(horizontal = 20.dp, vertical = 18.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (item.photographerImageUri.isNotEmpty()) {
                AsyncImage(
                    model = item.photographerImageUri,
                    contentDescription = item.photographerName,
                    modifier =
                        Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                )
            } else {
                Box(
                    modifier =
                        Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MainThemeColor.Gray2),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = item.photographerName, style = MainThemeFont.BodyBold)
                Text(
                    text = item.status,
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Green100,
                    modifier =
                        Modifier
                            .background(
                                MainThemeColor.Green30,
                                RoundedCornerShape(5.dp),
                            )
                            .border(
                                1.dp,
                                MainThemeColor.Green100,
                                RoundedCornerShape(5.dp),
                            )
                            .padding(horizontal = 6.dp, vertical = 1.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = item.packageName, style = MainThemeFont.Title)

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Icon(
                    painter = painterResource(id = CoreR.drawable.clock_green),
                    contentDescription = stringResource(R.string.my_page_shooting_date),
                    tint = MainThemeColor.Green120,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = stringResource(R.string.my_page_shooting_date),
                    style = MainThemeFont.Body,
                    color = MainThemeColor.Gray4,
                )
            }
            Text(
                text = item.shootingDate,
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Black,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Icon(
                    painter = painterResource(id = CoreR.drawable.location_green),
                    contentDescription = stringResource(R.string.my_page_shooting_location),
                    tint = MainThemeColor.Green120,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = stringResource(R.string.my_page_shooting_location),
                    style = MainThemeFont.Body,
                    color = MainThemeColor.Gray4,
                )
            }
            Text(
                text = item.shootingLocation,
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Black,
            )
        }
    }
}

@Composable
private fun MenuSection(onIntent: (MyPageIntent) -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = stringResource(R.string.my_page_follow_photographer),
            style = MainThemeFont.BodyLarge,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(MyPageIntent.NavigateToFollowedPhotographers) }
                    .padding(vertical = 12.dp),
        )
        Text(
            text = stringResource(R.string.my_page_my_reviews),
            style = MainThemeFont.BodyLarge,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(MyPageIntent.NavigateToMyReviews) }
                    .padding(vertical = 12.dp),
        )
        Text(
            text = stringResource(R.string.my_page_terms),
            style = MainThemeFont.BodyLarge,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(MyPageIntent.NavigateToTerms) }
                    .padding(vertical = 12.dp),
        )
    }
}

private val bannerHeight = 50.dp

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun MyPageScreenNoPhotographerPreview() {
    PicplzTheme {
        MyPageScreen(
            state =
                MyPageState(
                    nickname = "임두현",
                    hasPhotographerRole = false,
                ),
            onIntent = {},
            navController = rememberNavController(),
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun MyPageScreenWithPhotographerPreview() {
    PicplzTheme {
        MyPageScreen(
            state =
                MyPageState(
                    nickname = "임두현",
                    hasPhotographerRole = true,
                    photographerProfile =
                        PhotographerProfile(
                            displayName = "유가영 작가",
                            followerCount = 128,
                            packageCount = 0,
                            portfolioCount = 0,
                            instagramId = "@gayoung.photo",
                            isInstagramRegistered = true,
                            introduction = "감도 높은 자연광 프로필 촬영을 진행해요.",
                            regionSummary = "서울 마포구, 서울 용산구 외 16개 지역",
                            keywordSummary = "#캐주얼, #심플, #공주감성 외 3개 키워드",
                            equipmentSummary = "아이폰 16 PRO, 아이폰 X 외 3개 장비",
                            hasPackages = false,
                            satisfactionSummary =
                                PhotographerSatisfactionSummary(
                                    averageRating = "4.9",
                                    reviewCount = 48,
                                    repeatBookingRate = 82,
                                ),
                        ),
                ),
            onIntent = {},
            navController = rememberNavController(),
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun MyPageScreenWithPhotographerPackagePreview() {
    PicplzTheme {
        MyPageScreen(
            state =
                MyPageState(
                    nickname = "임두현",
                    hasPhotographerRole = true,
                    photographerProfile =
                        PhotographerProfile(
                            displayName = "유가영 작가",
                            followerCount = 128,
                            packageCount = 1,
                            portfolioCount = 0,
                            instagramId = "@gayoung.photo",
                            isInstagramRegistered = true,
                            introduction = "감도 높은 자연광 프로필 촬영을 진행해요.",
                            regionSummary = "서울 마포구, 서울 용산구 외 16개 지역",
                            keywordSummary = "#캐주얼, #심플, #공주감성 외 3개 키워드",
                            equipmentSummary = "아이폰 16 PRO, 아이폰 X 외 3개 장비",
                            hasPackages = true,
                            packagePreview =
                                PhotographerPackagePreview(
                                    imageUrl =
                                        "https://images.unsplash.com/photo-1513279922550-250c2129b13a" +
                                            "?auto=format&fit=crop&w=1200&q=80",
                                    title = "남친생기는 프사",
                                    price = 66000,
                                    meta = "프로필 촬영 · 30분",
                                    description = "원본 20장과 보정본 3장을 제공해요.",
                                ),
                            satisfactionSummary =
                                PhotographerSatisfactionSummary(
                                    averageRating = "4.9",
                                    reviewCount = 48,
                                    repeatBookingRate = 82,
                                ),
                        ),
                ),
            onIntent = {},
            navController = rememberNavController(),
        )
    }
}
