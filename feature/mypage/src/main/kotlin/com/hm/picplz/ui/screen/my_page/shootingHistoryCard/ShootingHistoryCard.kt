package com.hm.picplz.ui.screen.my_page.shootingHistoryCard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.BadgeTheme
import com.hm.picplz.ui.screen.common.CommonBadge
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

enum class PackageType(val label: String) {
    PROFILE("프로필 패키지"),
    KAKAO("카카오톡 패키지"),
    Instagram("인스타그램 패키지"),
}

enum class ReservationStatus(val label: String) {
    PENDING("예약 대기"), // 예약대기
    CONFIRMED("거래 확정"), // 거래확정
    INPROGRESS("촬영 진행"), // 촬영진행
}

enum class ShootingStatus(val label: String) {
    COMPLEETED("촬영 완료"),
    CANCLED("취소 완료"),
}

enum class ReservationType(val label: String) {
    ASAP("바로 촬영"), // ASAP
    NORMAL("일반 예약"), // 일반예약
}

@Composable
fun ShootingHistoryCard(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 23.5.dp,
    borderRadius: Dp = 5.dp,
    userName: String = "",
    userProfile: Int = R.drawable.default_profile,
    status: ShootingStatus = ShootingStatus.CANCLED,
    packageType: PackageType = PackageType.PROFILE,
    paymentDate: String = "2025.03.01",
    date: String = "",
    location: String = "",
    onClickOrderSheet: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(borderRadius))
                .border(1.dp, MainThemeColor.Gray2, RoundedCornerShape(borderRadius))
                .background(MainThemeColor.Gray1),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontalPadding, verticalPadding),
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Image(
                            painter = painterResource(id = userProfile),
                            contentDescription = "user-profile",
                            modifier =
                                Modifier
                                    .size(40.dp)
                                    .border(1.dp, MainThemeColor.Gray3, CircleShape)
                                    .clip(CircleShape),
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Text(text = userName, style = MainThemeFont.BodyBold)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(2.5.dp),
                            ) {
                                when (status) {
                                    ShootingStatus.CANCLED ->
                                        CommonBadge(
                                            label = status.label,
                                            theme = BadgeTheme.INACTIVE,
                                            modifier =
                                                Modifier.border(
                                                    1.dp,
                                                    MainThemeColor.Pink2,
                                                    RoundedCornerShape(5.dp),
                                                ),
                                        )

                                    ShootingStatus.COMPLEETED ->
                                        CommonBadge(
                                            label = status.label,
                                            theme = BadgeTheme.ACTIVE,
                                            modifier =
                                                Modifier.border(
                                                    1.dp,
                                                    MainThemeColor.Olive,
                                                    RoundedCornerShape(5.dp),
                                                ),
                                        )
                                }
                            }
                        }
                    }
                    Text(
                        text = "$paymentDate 결제",
                        style = MainThemeFont.Caption,
                        color = MainThemeColor.Gray4,
                    )
                }
                Spacer(modifier = Modifier.height(13.5.dp))
                Text(text = packageType.label, style = MainThemeFont.Title)
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MainThemeColor.Gray2,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.clock_green),
                            contentDescription = "clock",
                            modifier = Modifier.size(13.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "촬영 일시",
                            style = MainThemeFont.Body,
                            color = MainThemeColor.Gray4,
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(text = date, style = MainThemeFont.BodyBold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.location_green),
                                contentDescription = "clock",
                                modifier = Modifier.size(13.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "촬영 장소",
                                style = MainThemeFont.Body,
                                color = MainThemeColor.Gray4,
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(text = location, style = MainThemeFont.BodyBold)
                        }
                        Text(
                            text = "주문 상세",
                            style = MainThemeFont.Caption,
                            color = MainThemeColor.Gray4,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { onClickOrderSheet() },
                        )
                    }
                    Spacer(modifier = Modifier.height(23.dp))
                    CommonBottomButton(
                        text = if (status === ShootingStatus.COMPLEETED) "리뷰 쓰러가기" else "다시 예약하기",
                        onClick = { },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShootingHistoryCardCompletedPreview() {
    PicplzTheme {
        ShootingHistoryCard(
            userName = "합정동 불주먹",
            userProfile = R.drawable.edit_grey4,
            status = ShootingStatus.COMPLEETED,
            date = "5월 26일 오전 9시 30분",
            location = "종로구 효자로 33",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShootingHistoryCardCancledPreview() {
    PicplzTheme {
        ShootingHistoryCard(
            userName = "합정동 불주먹",
            userProfile = R.drawable.edit_grey4,
            status = ShootingStatus.CANCLED,
            date = "5월 26일 오전 9시 30분",
            location = "종로구 효자로 33",
        )
    }
}
