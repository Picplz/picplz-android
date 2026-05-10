package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.core.ui.R
import com.hm.picplz.domain.model.PhotographerInfo
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.pretendardTypography

private val followIconSize = 8.dp
private val followButtonWidth = 56.dp
private const val VISIBLE_AREA_COUNT = 3
private const val ARROW_INLINE_ID = "arrow"
private const val INFO_TOGGLE_ID = "toggle"

@Composable
fun DetailProfileSection(
    modifier: Modifier,
    profileInfo: PhotographerInfo,
    isFollow: Boolean,
    isInfoExpanded: Boolean,
    isAreaExpanded: Boolean,
    onToggleFollow: () -> Unit,
    onToggleInfoExpanded: () -> Unit,
    onToggleAreaExpanded: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    Spacer(modifier = Modifier.height(20.dp))

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
            var hasTextOverflow by remember { mutableStateOf(false) }
            if (isInfoExpanded) {
                // 펼침: 전체 텍스트 + "접기" 인라인
                val expandedText =
                    buildAnnotatedString {
                        append(profileInfo.infoText + " ")
                        appendInlineContent(INFO_TOGGLE_ID, "[접기]")
                    }
                val toggleContent =
                    mapOf(
                        INFO_TOGGLE_ID to
                            InlineTextContent(
                                placeholder =
                                    Placeholder(
                                        width = 24.sp,
                                        height = 14.sp,
                                        placeholderVerticalAlign =
                                            PlaceholderVerticalAlign.Center,
                                    ),
                            ) {
                                Text(
                                    text = stringResource(R.string.collapse),
                                    style = MainThemeFont.InnerTag,
                                    color = MainThemeColor.Green120,
                                    modifier =
                                        Modifier.clickable { onToggleInfoExpanded() },
                                )
                            },
                    )
                Text(
                    text = expandedText,
                    inlineContent = toggleContent,
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray6,
                )
            } else {
                // 접힘: 2줄 제한
                Text(
                    text = profileInfo.infoText,
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { result ->
                        hasTextOverflow = result.hasVisualOverflow
                    },
                )
                if (hasTextOverflow) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.expand_more),
                        style = MainThemeFont.InnerTag,
                        color = MainThemeColor.Gray4,
                        modifier =
                            Modifier.clickable { onToggleInfoExpanded() },
                    )
                }
            }
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
        values = profileInfo.workingArea.toDistrictNames(),
        visibleCount = VISIBLE_AREA_COUNT,
        isExpanded = isAreaExpanded,
        onToggleExpanded = onToggleAreaExpanded,
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
    val buttonModifier =
        Modifier
            .height(21.dp)
            .width(followButtonWidth)
    if (isFollow) {
        CommonIconButton(
            label = stringResource(R.string.following),
            backgroundColor = MainThemeColor.Black,
            textColor = MainThemeColor.White,
            textStyle = MainThemeFont.Caption,
            iconResId = R.drawable.following,
            iconSize = followIconSize,
            location = "right",
            horizontalPadding = 7.dp,
            verticalPadding = 3.dp,
            borderRadius = 5.dp,
            onClick = onToggleFollow,
            modifier = buttonModifier,
        )
    } else {
        CommonIconButton(
            label = stringResource(R.string.follow),
            backgroundColor = MainThemeColor.Gray2,
            textColor = MainThemeColor.Gray4,
            textStyle = MainThemeFont.Caption,
            iconResId = R.drawable.follow,
            iconSize = followIconSize,
            location = "right",
            horizontalPadding = 7.dp,
            verticalPadding = 3.dp,
            borderRadius = 5.dp,
            onClick = onToggleFollow,
            modifier = buttonModifier,
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
private fun AreaTextWithArrow(
    text: String,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val annotatedText =
        buildAnnotatedString {
            append("$text ")
            appendInlineContent(ARROW_INLINE_ID, "[>]")
        }

    val inlineContent =
        mapOf(
            ARROW_INLINE_ID to
                InlineTextContent(
                    placeholder =
                        Placeholder(
                            width = 10.sp,
                            height = 12.sp,
                            placeholderVerticalAlign =
                                PlaceholderVerticalAlign.TextCenter,
                        ),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_down),
                            contentDescription = null,
                            modifier =
                                Modifier
                                    .size(12.dp)
                                    .then(
                                        if (isExpanded) {
                                            Modifier.graphicsLayer { rotationZ = 180f }
                                        } else {
                                            Modifier
                                        },
                                    ),
                            tint = MainThemeColor.Gray3,
                        )
                    }
                },
        )

    Text(
        text = annotatedText,
        inlineContent = inlineContent,
        style = MainThemeFont.Caption,
        color = MainThemeColor.Gray4,
        modifier = modifier,
    )
}

@Composable
private fun InfoRow(
    modifier: Modifier,
    label: String,
    values: List<String>,
    visibleCount: Int,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
) {
    val needsOverflow = values.size > visibleCount

    if (isExpanded && needsOverflow) {
        // 확장 상태: 라벨 + 전체 목록 + 텍스트 끝에 "<" 인라인
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpanded() },
        ) {
            Text(
                text = label,
                style = MainThemeFont.Caption.copy(fontWeight = FontWeight.SemiBold),
                color = MainThemeColor.Black,
            )
            Spacer(modifier = Modifier.width(16.dp))
            AreaTextWithArrow(
                text = values.joinToString(", "),
                isExpanded = true,
                modifier = Modifier.weight(1f),
            )
        }
    } else {
        // 축약 상태
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .then(
                        if (needsOverflow) {
                            Modifier.clickable { onToggleExpanded() }
                        } else {
                            Modifier
                        },
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MainThemeFont.Caption.copy(fontWeight = FontWeight.SemiBold),
                color = MainThemeColor.Black,
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (!needsOverflow) {
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
                AreaTextWithArrow(
                    text = areaText,
                    isExpanded = false,
                )
            }
        }
    }
}

private fun List<String>.toDistrictNames(): List<String> =
    map { area ->
        area.split(" ").firstOrNull { it.endsWith("구") } ?: area
    }.distinct()
