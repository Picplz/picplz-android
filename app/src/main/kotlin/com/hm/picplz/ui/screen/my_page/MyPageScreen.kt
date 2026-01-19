package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hm.picplz.navigation.Routes
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.R
import com.hm.picplz.navigation.bottom_navigation.BottomNavigationBar
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.screen.common.PhotographerStatus
import com.hm.picplz.ui.screen.main.MainScreen
import com.hm.picplz.ui.screen.main.photographerCard.PhotographerCard
import com.hm.picplz.ui.screen.main.portfolioCard.PortfolioCard
import com.hm.picplz.ui.screen.main.scheduleCard.ScheduleCardNone
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

data class Profile(
    val profileImage: Int,
    val userName: String,
    val instagram: String,
    val introduction: String
)

@Composable
fun ProfileSection(navController: NavHostController) {
    val dummyProfile = Profile(
        profileImage = R.drawable.logo,
        userName = "임두현",
        instagram = "duduhyeon",
        introduction = "합정사는 임두현입니다."
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 15.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = dummyProfile.profileImage),
                    contentDescription = "profile-image",
                    modifier = Modifier
                        .size(70.dp)
                        .border(1.dp, MainThemeColor.Gray2, CircleShape)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column {
                    Text(text = dummyProfile.userName, style = MainThemeFont.TitleSmall)
                    Spacer(modifier = Modifier.height(9.8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.instagram),
                            contentDescription = "instagram"
                        )
                        Spacer(modifier = Modifier.width(2.4.dp))
                        Text(text = dummyProfile.instagram, color = MainThemeColor.Gray4)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = dummyProfile.introduction,
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray6
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = { navController.navigate(Routes.MY_PAGE_MODIFY_PROFILE) },
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, MainThemeColor.Gray3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 15.dp)
            ) {
                Text(
                    text = "프로필 수정",
                    style = MainThemeFont.ButtonDefault,
                    color = MainThemeColor.Black
                )
            }
        }
    }
}

@Composable
fun ReservationSection(navController: NavHostController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "진행중인 촬영", style = MainThemeFont.TitleSmall)
            CommonIconButton(
                label = "내 촬영 내역",
                backgroundColor = Color.Transparent,
                textColor = MainThemeColor.Gray60,
                textStyle = MainThemeFont.Caption,
                iconResId = R.drawable.depth_arrow,
                location = "right",
                horizontalPadding = 0.dp,
                verticalPadding = 0.dp,
                gap = 8.dp,
                onClick = { navController.navigate(Routes.MY_PAGE_SHOOTING_HISTORY) },
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
//        TODO: 예약 있을 때 없을 때 분기처리
//        ScheduleCard(
//            userName = "합정동 불주먹",
//            userProfile = R.drawable.edit_grey4,
//            status = ReservationStatus.INPROGRESS,
//            type = ReservationType.ASAP,
//            packageType = PackageType.KAKAO,
//            date = "5월 26일 오전 9시 30분",
//            location = "종로구 효자로 33",
//            onClick = {}
//        )
        ScheduleCardNone(
            mainText = "진행중인 촬영이 없어요",
            subText = "촬영지를 검색하고 작가들을 둘러보세요",
            onClick = { /* TODO */ })
    }
}


@Composable
fun FollowPhotographerSection() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "팔로우 작가", style = MainThemeFont.TitleSmall)
            CommonIconButton(
                label = "전체 목록",
                backgroundColor = Color.Transparent,
                textColor = MainThemeColor.Gray60,
                textStyle = MainThemeFont.Caption,
                iconResId = R.drawable.depth_arrow,
                location = "right",
                horizontalPadding = 0.dp,
                verticalPadding = 0.dp,
                gap = 8.dp,
                onClick = { },
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(
            modifier = Modifier.padding(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
//            TODO: 작가 데이터 연동
            item {
                PhotographerCard(
                    profileImage = R.drawable.user_undefined,
                    username = "유가영",
                    status = PhotographerStatus.ENABLED,
                    onClick = {}
                )
            }
            item {
                PhotographerCard(
                    profileImage = R.drawable.user_undefined,
                    username = "유가영",
                    status = PhotographerStatus.ENABLED,
                    onClick = {}
                )
            }
            item {
                PhotographerCard(
                    profileImage = R.drawable.user_undefined,
                    username = "유가영",
                    status = PhotographerStatus.ENABLED,
                    onClick = {}
                )
            }
            item {
                PhotographerCard(
                    profileImage = R.drawable.user_undefined,
                    username = "유가영",
                    status = PhotographerStatus.ENABLED,
                    onClick = {}
                )
            }
            item {
                PhotographerCard(
                    profileImage = R.drawable.user_undefined,
                    username = "유가영",
                    status = PhotographerStatus.DISABLED,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun ScrapSection() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "스크랩", style = MainThemeFont.TitleSmall)
            CommonIconButton(
                label = "전체 목록",
                backgroundColor = Color.Transparent,
                textColor = MainThemeColor.Gray60,
                textStyle = MainThemeFont.Caption,
                iconResId = R.drawable.depth_arrow,
                location = "right",
                horizontalPadding = 0.dp,
                verticalPadding = 0.dp,
                gap = 8.dp,
                onClick = { },
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(
            modifier = Modifier.padding(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
//            TODO: 작가 데이터 연동
            item {
                PortfolioCard(
                    portfolioImage = R.drawable.logo,
                    profileImage = R.drawable.user_undefined,
                    username = "유가영",
                    location = "무대륙",
                    bookmarkCnt = 23,
                    isBookMarked = false,
                    bookmarkClick = {},
                    onClick = {}
                )
            }
            item {
                PortfolioCard(
                    portfolioImage = R.drawable.logo,
                    profileImage = R.drawable.user_undefined,
                    username = "유가영",
                    location = "무대륙",
                    bookmarkCnt = 23,
                    isBookMarked = false,
                    bookmarkClick = {},
                    onClick = {}
                )
            }
            item {
                PortfolioCard(
                    portfolioImage = R.drawable.logo,
                    profileImage = R.drawable.user_undefined,
                    username = "유가영",
                    location = "무대륙",
                    bookmarkCnt = 23,
                    isBookMarked = false,
                    bookmarkClick = {},
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun EtcSection() {
    // TODO: 디자인 작업 완료 후 적용
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = "내 리뷰", style = MainThemeFont.ButtonDefault)
        Text(text = "문의하기", style = MainThemeFont.ButtonDefault)
        Text(text = "계정 정보 수정", style = MainThemeFont.ButtonDefault)
        Text(text = "이용 약관", style = MainThemeFont.ButtonDefault)
    }
}

@Composable
fun MyPageScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    var isToggled by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 11.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "마이 페이지",
                            style = MainThemeFont.BodySmallButton2,
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.setting),
                            contentDescription = "setting"
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MainThemeColor.Green120)
                            .padding(horizontal = 16.dp, vertical = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        TODO: 분기 처리
//                        Text(
//                            text = "작가로 전환",
//                            style = MainThemeFont.BodyBold,
//                            color = MainThemeColor.White
//                        )
//                        ToggleSwitch(
//                            checked = isToggled,
//                            onCheckedChange = { isToggled = it }
//                        )
                        Text(
                            text = "작가로도 활동하기",
                            style = MainThemeFont.BodyBold,
                            color = MainThemeColor.White
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.triangle_right),
                            contentDescription = "arrow",
                            tint = MainThemeColor.White,
                            modifier = Modifier.clickable { }
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
            )
        },
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())

        ) {
            ProfileSection(navController)

            Spacer(modifier = Modifier.height(28.5.dp))

            ReservationSection(navController)

            Spacer(modifier = Modifier.height(47.dp))

            FollowPhotographerSection()

            Spacer(modifier = Modifier.height(47.dp))

//            ScrapSection()
//
//            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(thickness = 10.dp, color = MainThemeColor.Gray1)

            Spacer(modifier = Modifier.height(20.dp))

            EtcSection()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        MainScreen(navController = navController)
    }
}
