package com.hm.picplz.ui.screen.my_page

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.screen.common.CommonDestructiveConfirmDialog
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.util.ReviewUtil
import com.hm.picplz.ui.util.StarType
import com.hm.picplz.core.ui.R as CoreUiR

private val DeletePillColor = Color(0xFFE83737)
private val MoreTextStyle =
    SpanStyle(
        color = MainThemeColor.Gray6,
        fontFamily = MainThemeFont.BodyBold.fontFamily,
        fontWeight = MainThemeFont.BodyBold.fontWeight,
        fontSize = MainThemeFont.BodyBold.fontSize,
        letterSpacing = MainThemeFont.BodyBold.letterSpacing,
    )

@Composable
fun MyReviewScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MyReviewViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is MyReviewSideEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    MyReviewScreenContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}

@Composable
internal fun MyReviewScreenContent(
    state: MyReviewState,
    onIntent: (MyReviewIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pendingDeleteReview = state.reviews.find { it.id == state.pendingDeleteReviewId }

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(
                title = stringResource(R.string.my_page_my_reviews),
                onClickBack = {
                    onIntent(MyReviewIntent.NavigateBack)
                },
            )
        },
        modifier =
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        if (state.reviews.isEmpty()) {
            MyReviewEmptyState(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding.toCenteredEmptyStatePadding()),
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 10.dp),
            ) {
                items(
                    items = state.reviews,
                    key = { review -> review.id },
                ) { review ->
                    MyReviewCard(
                        review = review,
                        isExpanded = review.id in state.expandedReviewIds,
                        onToggleExpanded = {
                            onIntent(MyReviewIntent.ToggleReviewExpansion(review.id))
                        },
                        onDeleteClick = {
                            onIntent(MyReviewIntent.RequestDelete(review.id))
                        },
                    )

                    if (review != state.reviews.last()) {
                        HorizontalDivider(
                            color = MainThemeColor.Gray2,
                            thickness = 1.dp,
                        )
                    }
                }
            }
        }
    }

    if (pendingDeleteReview != null) {
        MyReviewDeleteDialog(
            onDismiss = {
                onIntent(MyReviewIntent.DismissDeleteDialog)
            },
            onConfirm = {
                onIntent(MyReviewIntent.ConfirmDelete)
            },
        )
    }
}

private fun PaddingValues.toCenteredEmptyStatePadding(): PaddingValues =
    PaddingValues(
        top = calculateTopPadding() / 2,
        bottom = calculateBottomPadding(),
    )

@Composable
private fun MyReviewCard(
    review: MyReviewItem,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val starIcons = ReviewUtil.calculateStarRating(review.rating, StarType.SUB)

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(MainThemeColor.White)
                .padding(vertical = 20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            ReviewProfileImage(
                imageUri = review.photographerImageUri,
                contentDescription = review.photographerName,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.photographerName,
                    style = MainThemeFont.BodyBold,
                    color = MainThemeColor.Black,
                )
                Spacer(modifier = Modifier.height(1.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                    starIcons.forEach { iconRes ->
                        Image(
                            painter = painterResource(iconRes),
                            contentDescription = stringResource(CoreUiR.string.star_rating),
                            modifier = Modifier.size(15.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .height(17.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(DeletePillColor)
                            .clickable(onClick = onDeleteClick)
                            .padding(horizontal = 4.dp, vertical = 1.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.my_review_delete),
                        style = MainThemeFont.Caption.copy(fontSize = 10.sp, lineHeight = 14.sp),
                        color = MainThemeColor.White,
                    )
                }
                Text(
                    text = review.createdAt,
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray5,
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (review.imageUris.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                items(
                    items = review.imageUris,
                    key = { imageUri -> imageUri },
                ) { imageUri ->
                    AsyncImage(
                        model = imageUri,
                        contentDescription = stringResource(CoreUiR.string.review_photo),
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .size(113.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            ReviewMetaRow(
                label = stringResource(CoreUiR.string.option_label),
                value = review.option,
            )
            ReviewMetaRow(
                label = stringResource(CoreUiR.string.shooting_location),
                value = review.location,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        ExpandableReviewText(
            text = review.reviewText,
            isExpanded = isExpanded,
            onToggleExpanded = onToggleExpanded,
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(CoreUiR.drawable.like_inactive),
                contentDescription = stringResource(CoreUiR.string.like),
            )
            Text(
                text = review.likeCount.toString(),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
            )
        }
    }
}

@Composable
private fun ReviewProfileImage(
    imageUri: String,
    contentDescription: String,
) {
    if (imageUri.isNotBlank()) {
        AsyncImage(
            model = imageUri,
            contentDescription = stringResource(CoreUiR.string.user_profile),
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.dp, MainThemeColor.Gray3, CircleShape),
        )
    } else {
        Image(
            painter = painterResource(CoreUiR.drawable.default_profile),
            contentDescription = contentDescription,
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.dp, MainThemeColor.Gray3, CircleShape),
        )
    }
}

@Composable
private fun ReviewMetaRow(
    label: String,
    value: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = label,
            style = MainThemeFont.InnerTag,
            color = MainThemeColor.Gray4,
        )
        Text(
            text = value,
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
        )
    }
}

@Composable
private fun ExpandableReviewText(
    text: String,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
) {
    var isOverflowing by remember(text) { mutableStateOf(false) }
    val moreText = stringResource(R.string.my_review_more)
    val lessText = stringResource(R.string.my_review_less)
    var collapsedAnnotatedText by remember(text, moreText) {
        mutableStateOf(
            buildAnnotatedString {
                append(text)
            },
        )
    }

    if (isExpanded) {
        Text(
            text =
                buildAnnotatedString {
                    append(text)
                    append(" ")
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "toggle_less",
                            styles = TextLinkStyles(style = MoreTextStyle),
                            linkInteractionListener = { onToggleExpanded() },
                        ),
                    ) {
                        append(lessText)
                    }
                },
            style = MainThemeFont.Body.copy(color = MainThemeColor.Gray6),
        )
    } else {
        Text(
            text = collapsedAnnotatedText,
            style = MainThemeFont.Body.copy(color = MainThemeColor.Gray6),
            modifier = Modifier.clickable(enabled = isOverflowing, onClick = onToggleExpanded),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { layoutResult ->
                if (layoutResult.hasVisualOverflow) {
                    isOverflowing = true
                    val visibleTextEnd = layoutResult.getLineEnd(1, visibleEnd = true)
                    collapsedAnnotatedText = buildCollapsedReviewText(text, visibleTextEnd, moreText)
                }
            },
        )
    }
}

private fun buildCollapsedReviewText(
    text: String,
    visibleTextEnd: Int,
    moreText: String,
): androidx.compose.ui.text.AnnotatedString =
    buildAnnotatedString {
        val suffix = "...$moreText"
        val safeEnd = (visibleTextEnd - suffix.length).coerceAtLeast(0)
        append(text.take(safeEnd).trimEnd())
        append("...")
        withStyle(style = MoreTextStyle) {
            append(moreText)
        }
    }

@Composable
private fun MyReviewDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    CommonDestructiveConfirmDialog(
        title = stringResource(R.string.my_review_delete_dialog_title),
        description = stringResource(R.string.my_review_delete_dialog_description),
        cancelText = stringResource(R.string.my_review_delete_dialog_cancel),
        confirmText = stringResource(R.string.my_review_delete_dialog_confirm),
        onDismissRequest = onDismiss,
        onConfirm = onConfirm,
    )
}

@Composable
private fun MyReviewEmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.my_review_empty_title),
                style = MainThemeFont.TitleSmall,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                modifier = Modifier.size(64.dp),
                painter = painterResource(id = R.drawable.ic_shooting_history_empty),
                contentDescription = stringResource(R.string.my_review_empty_title),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.my_review_empty_description),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray5,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyReviewScreenPreview() {
    PicplzTheme {
        MyReviewScreenContent(
            state = MyReviewState.idle(),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyReviewEmptyStatePreview() {
    PicplzTheme {
        MyReviewScreenContent(
            state = MyReviewState(reviews = emptyList()),
            onIntent = {},
            modifier = Modifier,
        )
    }
}
