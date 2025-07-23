package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.R
import com.hm.picplz.navigation.bottom_navigation.BottomNavigationBar
import com.hm.picplz.ui.screen.common.PhotographerStatus
import com.hm.picplz.ui.screen.main.modalBottomSheet.DeviceModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.MoodKeywordModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.RegionModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.SortFilterModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType
import com.hm.picplz.ui.screen.main.photographerCard.PhotographerCard
import com.hm.picplz.ui.screen.main.portfolioCard.PortfolioCard
import com.hm.picplz.ui.screen.main.scheduleCard.ScheduleCardNone
import com.hm.picplz.ui.screen.main.search.SearchNavigateButton
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

private val DISTRICTS = listOf(
    "연남동", "성수동", "한남동", "망원동",
    "상수동", "이태원동", "청담동", "삼청동",
    "북촌동", "신사동", "서교동", "합정동",
    "논현동", "잠실동"
)
private val TODAY_DISTRICT: String by lazy { DISTRICTS.random() }

@Composable
fun SearchBanner(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(159.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MainThemeColor.Black, Color(0xFF242529))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(10.dp))

            SearchNavigateButton(
                placeholder = "촬영을 하고 싶은 장소 또는 동을 검색해보세요",
                onClick = { navController.navigate("main-search") })

            Spacer(Modifier.height(20.dp))

            Text(
                text = "오늘은 $TODAY_DISTRICT 나들이 어때요?",
                style = MainThemeFont.TitleSmall,
                color = MainThemeColor.White
            )

            Spacer(Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(MainThemeColor.Gray6)
                    .padding(5.dp)
            ) {
                // TODO: 작가 수 연동
                Text(
                    text = "N명의 작가가 픽플즈에서 활동하고 있어요",
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.White
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.mask_group),
            contentDescription = "banner-image",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .zIndex(1f)
        )
    }
}

@Composable
fun ReservationSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = "진행중인 촬영", style = MainThemeFont.TitleSmall)
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
fun AdSection() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(MainThemeColor.Olive)
        ) {
            Text(text = "광고!")
        }
    }
}

@Composable
fun PopularPortfolioSection() {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(text = "인기 포트폴리오", style = MainThemeFont.TitleSmall)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
//            TODO: 포트폴리오 데이터 연동
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
fun PopularPhotographerSection() {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(text = "인기 작가", style = MainThemeFont.TitleSmall)
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(
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
                    status = PhotographerStatus.DISABLED,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun PopularReviewSection() {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(text = "인기 리뷰", style = MainThemeFont.TitleSmall)
        Spacer(modifier = Modifier.height(10.dp))
//        TODO: 리뷰 데이터 연동
    }
}


@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    var visible by remember { mutableStateOf(false) } // <-- 상태 선언
    var visibleDevice by remember { mutableStateOf(false) } // <-- 상태 선언
    var visibleDeviceMood by remember { mutableStateOf(false) } // <-- 상태 선언
    var visibleSortFilter by remember { mutableStateOf(false) } // <-- 상태 선언

    var selectedSortType by remember { mutableStateOf(SortType.POPULAR) }

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MainThemeColor.Black)
                    .padding(16.dp, 11.dp)
            ) {
                Text(
                    text = "홈",
                    style = MainThemeFont.BodySmallButton2,
                    color = MainThemeColor.White
                )
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
            SearchBanner(navController = navController)

            Spacer(modifier = Modifier.height(30.dp))

            ReservationSection()

            Spacer(modifier = Modifier.height(30.dp))

            AdSection()

            Spacer(modifier = Modifier.height(30.dp))

            PopularPortfolioSection()

            Spacer(modifier = Modifier.height(30.dp))

            PopularPhotographerSection()

            Spacer(modifier = Modifier.height(30.dp))

            PopularReviewSection()




            Button(onClick = { navController.navigate("detail-photographer") }) {
                Text(text = "작사 상세 페이지 테스트 버튼")
            }
            Button(onClick = { navController.navigate("photographer-main") }) {
                Text(text = "작가 메인 페이지 테스트 버튼")
            }
            Button(onClick = { visible = true }) {
                Text(text = "지역 바텀시트 테스트 버튼")
            }
            Button(onClick = { visibleDevice = true }) {
                Text(text = "촬영기기 바텀시트 테스트 버튼")
            }
            Button(onClick = { visibleDeviceMood = true }) {
                Text(text = "분위기 키워드 바텀시트 테스트 버튼")
            }
            Row {
                Text(text = selectedSortType.label)
                Button(onClick = { visibleSortFilter = true }) {
                    Text(text = "정렬 바텀시트 테스트 버튼")
                }
            }

            RegionModalBottomSheet(
                onDismiss = { visible = false },
                visible = visible,
            )

            DeviceModalBottomSheet(
                onDismiss = { visibleDevice = false },
                visible = visibleDevice
            )

            MoodKeywordModalBottomSheet(
                onDismiss = { visibleDeviceMood = false },
                visible = visibleDeviceMood
            )

            SortFilterModalBottomSheet(
                onDismiss = { visibleSortFilter = false },
                visible = visibleSortFilter,
                onSelect = { selectedType ->
                    selectedSortType = selectedType
                }
            )
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
