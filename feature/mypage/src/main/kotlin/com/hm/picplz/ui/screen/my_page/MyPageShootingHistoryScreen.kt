package com.hm.picplz.ui.screen.my_page

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hm.picplz.navigation.model.MyPageOrderSheet
import com.hm.picplz.ui.screen.common.CommonEmptyState
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.screen.my_page.shootingHistoryCard.SwipeableShootingHistoryCard
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.core.ui.R as CoreUiR
import com.hm.picplz.feature.mypage.R as MyPageR

@Composable
fun MyPageShootingHistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ShootingHistoryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ShootingHistorySideEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is ShootingHistorySideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is ShootingHistorySideEffect.NavigateToOrderDetail -> {
                    navController.navigate(MyPageOrderSheet)
                }
            }
        }
    }

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(
                title = stringResource(CoreUiR.string.shooting_history_title),
            ) {
                viewModel.handleIntent(ShootingHistoryIntent.NavigateBack)
            }
        },
        modifier =
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        when {
            state.isLoading -> {
                ShootingHistoryLoadingState(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding.toCenteredEmptyStatePadding()),
                )
            }

            state.shootingHistories.isEmpty() -> {
                ShootingHistoryEmptyState(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding.toCenteredEmptyStatePadding()),
                )
            }

            else -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 15.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(
                            items = state.shootingHistories,
                            key = { it.id },
                        ) { item ->
                            SwipeableShootingHistoryCard(
                                photographerName = item.photographerName,
                                photographerImageUri = item.photographerImageUri,
                                productName = item.productName,
                                price = item.price,
                                status = item.status,
                                paymentDate = item.paymentDate,
                                shootingDate = item.shootingDate,
                                shootingLocation = item.shootingLocation,
                                hasChatRoom = item.hasChatRoom,
                                onDismiss = {
                                    viewModel.handleIntent(
                                        ShootingHistoryIntent.DeleteHistory(item.id),
                                    )
                                },
                                onClickChat = {
                                    viewModel.handleIntent(
                                        ShootingHistoryIntent.NavigateToChat(item.id),
                                    )
                                },
                                onClickReview = {
                                    viewModel.handleIntent(
                                        ShootingHistoryIntent.WriteReview(item.id),
                                    )
                                },
                                onClickOrderDetail = {
                                    viewModel.handleIntent(
                                        ShootingHistoryIntent.ViewOrderDetail(item.id),
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun PaddingValues.toCenteredEmptyStatePadding(): PaddingValues =
    PaddingValues(
        top = calculateTopPadding() / 2,
        bottom = calculateBottomPadding(),
    )

@Composable
private fun ShootingHistoryLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MainThemeColor.Black,
        )
    }
}

@Composable
private fun ShootingHistoryEmptyState(modifier: Modifier = Modifier) {
    CommonEmptyState(
        title = stringResource(CoreUiR.string.shooting_history_empty_title),
        description = stringResource(CoreUiR.string.shooting_history_empty_subtitle),
        imageResId = MyPageR.drawable.ic_shooting_history_empty,
        imageContentDescription = stringResource(CoreUiR.string.shooting_history_empty_char_desc),
        modifier = modifier,
    )
}
