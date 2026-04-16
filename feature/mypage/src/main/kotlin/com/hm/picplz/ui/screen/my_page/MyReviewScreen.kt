package com.hm.picplz.ui.screen.my_page

import CommonDialog
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.util.ReviewUtil
import com.hm.picplz.ui.util.StarType
import com.hm.picplz.core.ui.R as CoreUiR

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
                contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
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
                }
            }
        }
    }

    if (pendingDeleteReview != null) {
        MyReviewDeleteDialog(
            photographerName = pendingDeleteReview.photographerName,
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
                .border(1.dp, MainThemeColor.Gray2, RoundedCornerShape(5.dp))
                .background(MainThemeColor.White)
                .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            ReviewProfileImage(
                imageUri = review.photographerImageUri,
                contentDescription = review.photographerName,
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.photographerName,
                    style = MainThemeFont.BodyBold,
                    color = MainThemeColor.Black,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    starIcons.forEach { iconRes ->
                        Image(
                            painter = painterResource(iconRes),
                            contentDescription = stringResource(CoreUiR.string.star_rating),
                            modifier = Modifier.size(11.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.my_review_delete),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.White,
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(MainThemeColor.Red)
                            .clickable(onClick = onDeleteClick)
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                )
                Text(
                    text = review.createdAt,
                    style = MainThemeFont.Body,
                    color = MainThemeColor.Gray5,
                )
            }
        }

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
                                .size(114.dp)
                                .clip(RoundedCornerShape(5.dp)),
                    )
                }
            }
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

        ExpandableReviewText(
            text = review.reviewText,
            isExpanded = isExpanded,
            onToggleExpanded = onToggleExpanded,
        )

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
                    .size(37.dp)
                    .clip(CircleShape)
                    .border(1.dp, MainThemeColor.Gray3, CircleShape),
        )
    } else {
        Image(
            painter = painterResource(CoreUiR.drawable.default_profile),
            contentDescription = contentDescription,
            modifier =
                Modifier
                    .size(37.dp)
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

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = text,
            style = MainThemeFont.Body,
            color = MainThemeColor.Black,
            maxLines = if (isExpanded) Int.MAX_VALUE else 2,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { layoutResult ->
                if (!isExpanded) {
                    isOverflowing = layoutResult.hasVisualOverflow
                }
            },
        )

        if (isExpanded || isOverflowing) {
            Text(
                text = stringResource(if (isExpanded) R.string.my_review_less else R.string.my_review_more),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
                modifier = Modifier.clickable(onClick = onToggleExpanded),
            )
        }
    }
}

@Composable
private fun MyReviewDeleteDialog(
    photographerName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    CommonDialog(
        onDismissRequest = onDismiss,
        hasQuit = false,
    ) {
        Column {
            Text(
                text = stringResource(R.string.my_review_delete_dialog_title),
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Black,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.my_review_delete_dialog_description, photographerName),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray5,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(
                        text = stringResource(R.string.my_review_delete_dialog_cancel),
                        style = MainThemeFont.Body,
                        color = MainThemeColor.Gray5,
                    )
                }

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(5.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MainThemeColor.Red,
                            contentColor = MainThemeColor.White,
                        ),
                ) {
                    Text(
                        text = stringResource(R.string.my_review_delete_dialog_confirm),
                        style = MainThemeFont.Body,
                    )
                }
            }
        }
    }
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
