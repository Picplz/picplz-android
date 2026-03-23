package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.core.ui.R
import com.hm.picplz.data.model.PhotographerInfo
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.pretendardTypography

private const val VISIBLE_AREA_COUNT = 3

@Composable
fun DetailProfileSection(
    modifier: Modifier,
    profileInfo: PhotographerInfo,
    isFollow: Boolean,
    isInfoExpanded: Boolean,
    onToggleFollow: () -> Unit,
    onToggleInfoExpanded: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Image(
            rememberAsyncImagePainter(model = profileInfo.profileImageUri),
            contentDescription = stringResource(R.string.photographer_profile_image),
            modifier =
                Modifier
                    .size(74.dp)
                    .clip(CircleShape)
                    .border(1.dp, MainThemeColor.Gray2, CircleShape),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            // 이름 + 112명 + 팔로우 버튼 (같은 줄)
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text =
                        stringResource(
                            R.string.photographer_name_format,
                            profileInfo.name,
                        ),
                    style = MainThemeFont.TitleSmall,
                    color = MainThemeColor.Black,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text =
                        stringResource(
                            R.string.follower_count_format,
                            profileInfo.followCount,
                        ),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray4,
                )
                Spacer(modifier = Modifier.width(8.dp))
                FollowButton(
                    isFollow = isFollow,
                    onToggleFollow = onToggleFollow,
                )
            }

            // 인스타그램
            if (profileInfo.socialAccount != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.instagram),
                        contentDescription = stringResource(R.string.instagram),
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = profileInfo.socialAccount.toString(),
                        style =
                            pretendardTypography.bodySmall.copy(
                                fontWeight = FontWeight.Normal,
                                textDecoration = TextDecoration.Underline,
                            ),
                        color = MainThemeColor.Black,
                        modifier =
                            Modifier.clickable {
                                uriHandler.openUri(
                                    "https://www.instagram.com/${profileInfo.socialAccount}/",
                                )
                            },
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 소개글 — Caption, Gray6
            Text(
                text = profileInfo.infoText,
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray6,
                maxLines = if (isInfoExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text =
                    if (isInfoExpanded) {
                        stringResource(R.string.collapse)
                    } else {
                        stringResource(R.string.expand_more)
                    },
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
                modifier = Modifier.clickable { onToggleInfoExpanded() },
            )
        }
    }

    HorizontalDivider(
        color = MainThemeColor.Gray2,
        thickness = 1.dp,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
    )

    InfoRow(
        modifier = modifier,
        label = stringResource(R.string.shooting_area),
        values = profileInfo.workingArea,
        visibleCount = VISIBLE_AREA_COUNT,
    )

    Spacer(modifier = Modifier.height(10.dp))

    LabelValueRow(
        modifier = modifier,
        label = stringResource(R.string.keyword),
        value = profileInfo.keyword.joinToString(", "),
    )

    Spacer(modifier = Modifier.height(10.dp))

    LabelValueRow(
        modifier = modifier,
        label = stringResource(R.string.equipment),
        value = profileInfo.equipment.joinToString(", "),
    )
}

@Composable
private fun FollowButton(
    isFollow: Boolean,
    onToggleFollow: () -> Unit,
) {
    if (isFollow) {
        CommonIconButton(
            label = stringResource(R.string.following),
            backgroundColor = MainThemeColor.Black,
            textColor = MainThemeColor.White,
            textStyle = MainThemeFont.Caption,
            iconResId = R.drawable.following,
            location = "right",
            horizontalPadding = 7.dp,
            verticalPadding = 3.dp,
            borderRadius = 5.dp,
            onClick = onToggleFollow,
            modifier = Modifier.height(25.dp),
        )
    } else {
        CommonIconButton(
            label = stringResource(R.string.follow),
            backgroundColor = MainThemeColor.White,
            textColor = MainThemeColor.Gray3,
            textStyle = MainThemeFont.Caption,
            iconResId = R.drawable.follow,
            location = "right",
            horizontalPadding = 7.dp,
            verticalPadding = 3.dp,
            borderRadius = 5.dp,
            onClick = onToggleFollow,
            modifier =
                Modifier
                    .height(25.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        width = 1.dp,
                        color = MainThemeColor.Gray2,
                        shape = RoundedCornerShape(5.dp),
                    ),
        )
    }
}

@Composable
private fun LabelValueRow(
    modifier: Modifier,
    label: String,
    value: String,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MainThemeFont.Caption.copy(fontWeight = FontWeight.SemiBold),
            color = MainThemeColor.Black,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = value,
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
        )
    }
}

@Composable
private fun InfoRow(
    modifier: Modifier,
    label: String,
    values: List<String>,
    visibleCount: Int,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MainThemeFont.Caption.copy(fontWeight = FontWeight.SemiBold),
            color = MainThemeColor.Black,
        )
        Spacer(modifier = Modifier.width(16.dp))
        if (values.size <= visibleCount) {
            Text(
                text = values.joinToString(", "),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
        } else {
            val areaText =
                values.take(visibleCount).joinToString(", ") +
                    " " +
                    stringResource(
                        R.string.area_overflow_format,
                        values.size - visibleCount,
                    )
            Text(
                text = areaText,
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.depth_arrow),
                contentDescription = stringResource(R.string.expand_more),
                modifier = Modifier.size(12.dp),
                tint = MainThemeColor.Gray3,
            )
        }
    }
}
