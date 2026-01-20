package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.screen.my_page.shootingHistoryCard.PackageType
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

// 1) 모델 정의
data class OrderInfo(
    val orderNum: String,
    val orderDate: String,
    val clientName: String,
    val clientPhoneNumber: String,
    val photographerName: String,
    val packageType: PackageType,
    val packageCost: Int,
    val packageInfo: String,
    val saleCost: Int,
    val cost: Int,
    val shootingTime: String,
    val shootingLocation: String,
    val packageImage: Int,
)

@Composable
fun MyPageOrderSheetScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val orderDummyInfo = OrderInfo(
        orderNum = "nnnnmmmmdd123456",
        orderDate = "2025-03-09 19:09:14",
        clientName = "주은강",
        clientPhoneNumber = "01012345678",
        photographerName = "주로그",
        packageType = PackageType.PROFILE,
        packageCost = 12000,
        packageInfo = "본인이 해놓은 한줄 소개 길어지면 이렇게 처리해주세요ㅇ모ㅓㅏ로망리ㅗㅁ어라ㅣ멍;ㅣㄹ",
        saleCost = 0,
        cost = 12000,
        shootingTime = "25.01.09 - 오후 02:00",
        shootingLocation = "서울특별시 종로구 효자로 33, 네번째 테이블 창문 앞",
        packageImage = R.drawable.ic_launcher_background
    )
    val scrollState = rememberScrollState()


    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(title = "주문서 확인") {
                navController.popBackStack()
            }
        },
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 15.dp),
            ) {
                Text(text = "주문번호", style = MainThemeFont.ButtonDefault)
                Column {
                    Text(
                        text = orderDummyInfo.orderNum,
                        style = MainThemeFont.BodyBold,
                        color = Color(0XFFEF4747)
                    )
                    Text(
                        text = orderDummyInfo.orderDate,
                        style = MainThemeFont.Caption,
                        color = MainThemeColor.Gray4
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(text = "주문 고객 정보", style = MainThemeFont.ButtonDefault)
                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Text(text = "이름", style = MainThemeFont.InnerTag, color = MainThemeColor.Gray4)
                    Text(text = orderDummyInfo.clientName, style = MainThemeFont.Body)
                }
                Spacer(modifier = Modifier.height(9.dp))
                Column {
                    Text(
                        text = "휴대폰 번호",
                        style = MainThemeFont.InnerTag,
                        color = MainThemeColor.Gray4
                    )
                    Text(text = orderDummyInfo.clientPhoneNumber, style = MainThemeFont.Body)
                }

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "작가명", style = MainThemeFont.ButtonDefault)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = orderDummyInfo.photographerName,
                    style = MainThemeFont.Body,
                    color = MainThemeColor.Gray5
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(text = "주문 상품 정보", style = MainThemeFont.ButtonDefault)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MainThemeColor.Black, RoundedCornerShape(5.dp))
                        .padding(14.dp)
                ) {
                    Row {
                        Image(
                            painter = painterResource(id = orderDummyInfo.packageImage),
                            contentDescription = "package-image",
                            modifier = Modifier
                                .size(68.dp)
                                .clip(RoundedCornerShape(5.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = orderDummyInfo.packageType.label,
                                style = MainThemeFont.TitleSmall
                            )
                            Text(
                                text = "${orderDummyInfo.packageCost.toString()}₩",
                                style = MainThemeFont.Body
                            )
                            Spacer(modifier = Modifier.height(9.dp))
                            Text(
                                text = orderDummyInfo.packageInfo,
                                style = MainThemeFont.Caption,
                                color = MainThemeColor.Gray4,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(text = "촬영 희망 시각", style = MainThemeFont.ButtonDefault)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = orderDummyInfo.orderDate,
                    style = MainThemeFont.Body,
                    color = MainThemeColor.Gray5
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(text = "촬영 장소", style = MainThemeFont.ButtonDefault)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = orderDummyInfo.shootingLocation, style = MainThemeFont.Body,
                    color = MainThemeColor.Gray5
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .height(95.dp)
                        .fillMaxWidth()
                        .background(MainThemeColor.Black)
                )

                Spacer(modifier = Modifier.height(30.dp))

                HorizontalDivider(thickness = 10.dp, color = MainThemeColor.Gray1)

                Spacer(modifier = Modifier.height(30.dp))

                Text(text = "최종 결제 금액", style = MainThemeFont.ButtonDefault)
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Black)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "상품 금액")
                    Text(text = "${orderDummyInfo.packageCost}원")
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "총 할인 금액")
                    Text(text = "${orderDummyInfo.saleCost}원")
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Black)

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "결제 금액", style = MainThemeFont.ButtonDefault)
                    Text(
                        text = "${orderDummyInfo.cost}원(취소 완료)",
                        style = MainThemeFont.ButtonDefault,
                        color = MainThemeColor.Red
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MainThemeColor.Gray3, RoundedCornerShape(5.dp))
                        .background(MainThemeColor.Gray1)
                        .padding(horizontal = 13.dp, vertical = 10.dp)
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "신용/체크카드",
                                style = MainThemeFont.BodyBold,
                                color = MainThemeColor.Gray5
                            )
                            Text(
                                text = "${orderDummyInfo.cost}원",
                                style = MainThemeFont.Body,
                                color = MainThemeColor.Gray5
                            )
                        }
                        Row {
                            Text(
                                text = "신한카드(*********5199)",
                                style = MainThemeFont.Caption,
                                color = MainThemeColor.Gray5
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "일시불",
                                style = MainThemeFont.Caption,
                                color = MainThemeColor.Gray5
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = orderDummyInfo.orderDate,
                            style = MainThemeFont.Body,
                            color = MainThemeColor.Gray3
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                CommonBottomButton(
                    text = "영수증 조회",
                    onClick = { /*TODO*/ },
                    contentColor = MainThemeColor.Black,
                    containerColor = MainThemeColor.White,
                    borderColor = MainThemeColor.Gray3,
                    modifier = Modifier.height(42.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}