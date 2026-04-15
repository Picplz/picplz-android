package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun FollowedPhotographersScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: FollowedPhotographersViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is FollowedPhotographersSideEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is FollowedPhotographersSideEffect.NavigateToPhotographerDetail -> {
                    navController.navigate(DetailPhotographer(sideEffect.photographerId))
                }
            }
        }
    }

    FollowedPhotographersScreenContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}

@Composable
internal fun FollowedPhotographersScreenContent(
    state: FollowedPhotographersState,
    onIntent: (FollowedPhotographersIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(
                title = stringResource(R.string.my_page_follow_photographer),
                onClickBack = {
                    onIntent(FollowedPhotographersIntent.NavigateBack)
                },
            )
        },
        modifier =
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        when {
            state.isLoading -> {
                FollowedPhotographersLoading(
                    modifier = Modifier.padding(innerPadding),
                )
            }

            state.isUnavailable -> {
                FollowedPhotographersUnavailable(
                    modifier = Modifier.padding(innerPadding),
                )
            }

            state.photographers.isEmpty() -> {
                FollowedPhotographersEmpty(
                    modifier = Modifier.padding(innerPadding),
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    items(
                        items = state.photographers,
                        key = { photographer -> photographer.id },
                    ) { photographer ->
                        FollowedPhotographerCard(
                            photographer = photographer,
                            onClick = {
                                onIntent(
                                    FollowedPhotographersIntent.SelectPhotographer(photographer.id),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FollowedPhotographerCard(
    photographer: FollowedPhotographerItem,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            FollowedPhotographerThumbnail(
                imageUri = photographer.profileImageUri,
                contentDescription = photographer.name,
            )

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .heightIn(min = 88.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = photographer.name,
                            style = MainThemeFont.BodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = MainThemeColor.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )

                        if (photographer.isBookable) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .size(10.dp)
                                            .clip(RoundedCornerShape(999.dp))
                                            .background(MainThemeColor.Green120),
                                )
                                Text(
                                    text = stringResource(R.string.followed_photographers_available_now),
                                    style = MainThemeFont.Body,
                                    color = MainThemeColor.Green120,
                                    maxLines = 1,
                                )
                            }
                        }
                    }

                    Text(
                        text = formatWorkingAreas(photographer.workingAreas),
                        style = MainThemeFont.Body,
                        color = MainThemeColor.Gray4,
                    )
                }

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    photographer.keywords.forEach { keyword ->
                        KeywordChip(text = keyword)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)
    }
}

@Composable
private fun FollowedPhotographerThumbnail(
    imageUri: String,
    contentDescription: String,
) {
    if (imageUri.isNotBlank()) {
        AsyncImage(
            model = imageUri,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(5.dp)),
        )
    } else {
        Box(
            modifier =
                Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .semantics {
                        this.contentDescription = contentDescription
                    }
                    .background(MainThemeColor.Gray2),
        )
    }
}

@Composable
private fun KeywordChip(text: String) {
    Box(
        modifier =
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MainThemeColor.Gray1)
                .height(30.dp)
                .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
            maxLines = 1,
        )
    }
}

@Composable
private fun FollowedPhotographersLoading(modifier: Modifier = Modifier) {
    val loadingDescription = stringResource(R.string.followed_photographers_loading)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clearAndSetSemantics {
                    contentDescription = loadingDescription
                },
    ) {
        repeat(5) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box(
                        modifier =
                            Modifier
                                .size(88.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MainThemeColor.Gray1),
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        LoadingLine(width = 132.dp)
                        LoadingLine(width = 150.dp)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            LoadingChip()
                            LoadingChip()
                            LoadingChip()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)
            }
        }
    }
}

@Composable
private fun LoadingLine(width: androidx.compose.ui.unit.Dp) {
    Box(
        modifier =
            Modifier
                .width(width)
                .height(18.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MainThemeColor.Gray1),
    )
}

@Composable
private fun LoadingChip() {
    Box(
        modifier =
            Modifier
                .width(82.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MainThemeColor.Gray1),
    )
}

@Composable
private fun FollowedPhotographersUnavailable(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, MainThemeColor.Gray2, RoundedCornerShape(24.dp))
                    .background(MainThemeColor.Gray1),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.followed_photographers_unavailable_title),
            style = MainThemeFont.Title,
            color = MainThemeColor.Black,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.followed_photographers_unavailable_description),
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
        )
    }
}

@Composable
private fun FollowedPhotographersEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, MainThemeColor.Gray2, RoundedCornerShape(24.dp))
                    .background(MainThemeColor.Gray1),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.followed_photographers_empty_title),
            style = MainThemeFont.Title,
            color = MainThemeColor.Black,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.followed_photographers_empty_description),
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
        )
    }
}

@Composable
private fun formatWorkingAreas(workingAreas: List<String>): String {
    return when {
        workingAreas.isEmpty() -> ""
        workingAreas.size <= 3 -> workingAreas.joinToString(separator = ", ")
        else -> {
            val visibleAreas = workingAreas.take(3).joinToString(separator = ", ")
            val remainingCount = workingAreas.size - 3
            val overflowLabel = stringResource(R.string.followed_photographers_area_overflow, remainingCount)
            "$visibleAreas $overflowLabel"
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FollowedPhotographersScreenPreview() {
    PicplzTheme {
        FollowedPhotographersScreenContent(
            state =
                FollowedPhotographersState(
                    isLoading = false,
                    photographers =
                        listOf(
                            FollowedPhotographerItem(
                                id = 1,
                                name = "유가영 작가",
                                profileImageUri = "",
                                workingAreas = listOf("강남구", "강북구", "동대문구", "송파구"),
                                keywords = listOf("#을지로 감성", "#MZ 감성", "#필름톤"),
                                isBookable = true,
                            ),
                        ),
                ),
            onIntent = {},
        )
    }
}
