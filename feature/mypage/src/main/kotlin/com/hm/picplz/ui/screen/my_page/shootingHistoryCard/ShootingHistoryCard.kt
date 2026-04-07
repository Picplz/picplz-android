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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.BadgeTheme
import com.hm.picplz.ui.screen.common.CommonBadge
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import java.text.NumberFormat
import java.util.Locale

enum class PackageType(val label: String) {
    PROFILE("프로필 패키지"),
    KAKAO("카카오톡 패키지"),
    Instagram("인스타그램 패키지"),
}

enum class ReservationStatus(val label: String) {
    PENDING("예약 대기"),
    CONFIRMED("거래 확정"),
    INPROGRESS("촬영 진행"),
}

enum class ShootingStatus(val label: String) {
    COMPLETED("촬영 완료"),
    CANCELLED("촬영 취소"),
}

enum class ReservationType(val label: String) {
    ASAP("바로 촬영"),
    NORMAL("일반 예약"),
}

private object ShootingHistoryCardDefaults {
    val CardCornerRadius = 5.dp
    val CardHorizontalPadding = 18.dp
    val CardVerticalPadding = 20.dp
    val ProfileImageSize = 24.dp
    val ContentGap = 12.dp
}

@Composable
fun ShootingHistoryCard(
    modifier: Modifier = Modifier,
    photographerName: String,
    photographerImageUri: String,
    productName: String,
    price: Int,
    status: ShootingStatus,
    paymentDate: String,
    shootingDate: String,
    shootingLocation: String,
    hasChatRoom: Boolean = true,
    onClickChat: () -> Unit = {},
    onClickReview: () -> Unit = {},
    onClickOrderDetail: () -> Unit = {},
) {
    val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(price)

    Box(
        modifier =
            modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(ShootingHistoryCardDefaults.CardCornerRadius))
                .border(
                    1.dp,
                    MainThemeColor.Gray2,
                    RoundedCornerShape(ShootingHistoryCardDefaults.CardCornerRadius),
                )
                .background(MainThemeColor.White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ShootingHistoryCardDefaults.CardHorizontalPadding,
                        vertical = ShootingHistoryCardDefaults.CardVerticalPadding,
                    ),
            verticalArrangement = Arrangement.spacedBy(ShootingHistoryCardDefaults.ContentGap),
        ) {
            Column {
                PhotographerHeader(
                    photographerName = photographerName,
                    photographerImageUri = photographerImageUri,
                    paymentDate = paymentDate,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = productName, style = MainThemeFont.Body)
                Spacer(modifier = Modifier.height(4.dp))
                PriceBadgeRow(
                    formattedPrice = formattedPrice,
                    status = status,
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = MainThemeColor.Gray2,
            )

            ShootingDateLocationSection(
                shootingDate = shootingDate,
                shootingLocation = shootingLocation,
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { onClickOrderDetail() },
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.shooting_history_view_order_detail),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray3,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    painter = painterResource(id = R.drawable.depth_arrow),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = MainThemeColor.Gray3,
                )
            }

            ActionButtons(
                status = status,
                hasChatRoom = hasChatRoom,
                onClickChat = onClickChat,
                onClickReview = onClickReview,
            )
        }
    }
}

@Composable
private fun PhotographerHeader(
    photographerName: String,
    photographerImageUri: String,
    paymentDate: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (photographerImageUri.isNotEmpty()) {
                AsyncImage(
                    model = photographerImageUri,
                    contentDescription = stringResource(R.string.user_profile),
                    modifier =
                        Modifier
                            .size(ShootingHistoryCardDefaults.ProfileImageSize)
                            .border(1.dp, MainThemeColor.Gray3, CircleShape)
                            .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.default_profile),
                    contentDescription = stringResource(R.string.user_profile),
                    modifier =
                        Modifier
                            .size(ShootingHistoryCardDefaults.ProfileImageSize)
                            .border(1.dp, MainThemeColor.Gray3, CircleShape)
                            .clip(CircleShape),
                )
            }
            Text(text = photographerName, style = MainThemeFont.BodyBold)
        }
        Text(
            text = stringResource(R.string.shooting_history_payment_date, paymentDate),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray3,
        )
    }
}

@Composable
private fun PriceBadgeRow(
    formattedPrice: String,
    status: ShootingStatus,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = stringResource(R.string.shooting_history_price_won, formattedPrice),
            style = MainThemeFont.BodyLarge,
            fontWeight = FontWeight.Bold,
            color = MainThemeColor.Gray6,
        )
        CommonBadge(
            label = status.label,
            theme =
                when (status) {
                    ShootingStatus.COMPLETED -> BadgeTheme.ACTIVE
                    ShootingStatus.CANCELLED -> BadgeTheme.INACTIVE
                },
            modifier =
                Modifier.border(
                    1.dp,
                    when (status) {
                        ShootingStatus.COMPLETED -> MainThemeColor.Green100
                        ShootingStatus.CANCELLED -> MainThemeColor.Pink2
                    },
                    RoundedCornerShape(5.dp),
                ),
        )
    }
}

@Composable
private fun ShootingDateLocationSection(
    shootingDate: String,
    shootingLocation: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Row {
            Text(
                text = stringResource(R.string.shooting_history_date_label),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.width(12.dp))
            ShootingDateText(shootingDate)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.shooting_history_location_label),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = shootingLocation,
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Gray5,
            )
        }
    }
}

@Composable
private fun ShootingDateText(shootingDate: String) {
    val parts = shootingDate.split("|").map { it.trim() }
    Row {
        Text(text = parts[0], style = MainThemeFont.BodyBold, color = MainThemeColor.Gray5)
        if (parts.size > 1) {
            Text(text = " | ", style = MainThemeFont.BodyBold, color = MainThemeColor.Gray3)
            Text(text = parts[1], style = MainThemeFont.BodyBold, color = MainThemeColor.Gray5)
        }
    }
}

@Composable
private fun ActionButtons(
    status: ShootingStatus,
    hasChatRoom: Boolean,
    onClickChat: () -> Unit,
    onClickReview: () -> Unit,
) {
    when (status) {
        ShootingStatus.CANCELLED -> {
            ShootingHistoryButton(
                text = stringResource(R.string.shooting_history_go_chat),
                onClick = onClickChat,
                enabled = hasChatRoom,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        ShootingStatus.COMPLETED -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ShootingHistoryButton(
                    text = stringResource(R.string.shooting_history_go_chat),
                    onClick = onClickChat,
                    enabled = hasChatRoom,
                    modifier = Modifier.weight(1f),
                )
                ShootingHistoryButton(
                    text = stringResource(R.string.shooting_history_write_review),
                    onClick = onClickReview,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ShootingHistoryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val backgroundColor = if (enabled) MainThemeColor.White else MainThemeColor.Gray2
    val textColor = if (enabled) MainThemeColor.Gray6 else MainThemeColor.Gray3
    val borderModifier =
        if (enabled) {
            Modifier.border(1.dp, MainThemeColor.Gray3, RoundedCornerShape(5.dp))
        } else {
            Modifier
        }

    Box(
        modifier =
            modifier
                .height(40.dp)
                .clip(RoundedCornerShape(5.dp))
                .then(borderModifier)
                .background(backgroundColor)
                .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MainThemeFont.Body,
            color = textColor,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun ShootingHistoryCardCancelledPreview() {
    PicplzTheme {
        ShootingHistoryCard(
            photographerName = "합정동작가",
            photographerImageUri = "",
            productName = "남친생기는 프사",
            price = 12000,
            status = ShootingStatus.CANCELLED,
            paymentDate = "2025.03.01",
            shootingDate = "25.03.24 | 오후2:30",
            shootingLocation = "종로구 효자로 33 어디어디빌딩",
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun ShootingHistoryCardCompletedPreview() {
    PicplzTheme {
        ShootingHistoryCard(
            photographerName = "유가영사진",
            photographerImageUri = "",
            productName = "인스타 피드꾸미기",
            price = 12000,
            status = ShootingStatus.COMPLETED,
            paymentDate = "2025.03.01",
            shootingDate = "25.03.24 | 오후2:30",
            shootingLocation = "종로구 효자로 33 어디어디 어디",
        )
    }
}
