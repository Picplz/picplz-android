package com.hm.picplz.ui.screen.my_page

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.navigation.model.MyPageModifyProfile
import com.hm.picplz.navigation.model.MyPageMyReviews
import com.hm.picplz.navigation.model.MyPageShootingHistory
import com.hm.picplz.ui.navigation.BottomNavigationBar
import com.hm.picplz.ui.screen.my_page.toggleSwitch.ToggleSwitch
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.core.ui.R as CoreR

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is MyPageSideEffect.NavigateToModifyProfile -> {
                    navController.navigate(MyPageModifyProfile)
                }
                is MyPageSideEffect.NavigateToMyReviews -> {
                    navController.navigate(MyPageMyReviews)
                }
                is MyPageSideEffect.NavigateToShootingHistory -> {
                    navController.navigate(MyPageShootingHistory)
                }
                is MyPageSideEffect.NavigateToSettings -> {
                    Toast.makeText(context, "설정 기능은 준비 중입니다.", Toast.LENGTH_SHORT).show()
                }
                is MyPageSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    MyPageScreen(
        state = state,
        onIntent = viewModel::handleIntent,
        navController = navController,
        modifier = modifier,
    )
}

@Composable
private fun MyPageScreen(
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
            BottomNavigationBar(navController = navController)
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

@OptIn(ExperimentalFoundationApi::class)
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
                contentDescription = stringResource(R.string.my_page_title),
                modifier =
                    Modifier
                        .size(23.dp)
                        .combinedClickable(
                            onClick = { onIntent(MyPageIntent.NavigateToSettings) },
                            onLongClick = { onIntent(MyPageIntent.DevToggleShootings) },
                        ),
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
            checked = false,
            onCheckedChange = { onIntent(MyPageIntent.SwitchToPhotographer) },
            width = 56.dp,
            height = 28.dp,
            thumbSize = 19.dp,
            trackColor = MainThemeColor.Gray1,
            trackStroke = MainThemeColor.Gray2,
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
                ),
            onIntent = {},
            navController = rememberNavController(),
        )
    }
}
