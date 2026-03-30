package com.hm.picplz.ui.screen.order_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.order_detail.composable.CustomerInfoSection
import com.hm.picplz.ui.screen.order_detail.composable.OrderDetailTopBar
import com.hm.picplz.ui.screen.order_detail.composable.OrderNumberSection
import com.hm.picplz.ui.screen.order_detail.composable.PhotographerSection
import com.hm.picplz.ui.screen.order_detail.composable.ProductInfoSection
import com.hm.picplz.ui.screen.order_detail.composable.ScheduleSection
import com.hm.picplz.ui.theme.MainThemeColor.Gray2

@Composable
fun OrderDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateNextStep: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrderDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is OrderDetailSideEffect.NavigateBack -> onNavigateBack()
                is OrderDetailSideEffect.NavigateToNextStep -> onNavigateNextStep()
            }
        }
    }

    OrderDetailScreenContent(
        modifier = modifier,
        state = state,
        onBackClick = {
            viewModel.handleIntent(OrderDetailIntent.OnBackClick)
        },
        onNextClick = {
            viewModel.handleIntent(OrderDetailIntent.OnNextClick)
        },
    )
}

@Composable
private fun OrderDetailScreenContent(
    state: OrderDetailState,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            OrderDetailTopBar(
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
            ) {
                item {
                    OrderNumberSection(
                        orderNumber = state.orderNumber,
                        orderTime = state.orderTime,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                item {
                    CustomerInfoSection(
                        customerName = state.customerName,
                        phoneNumber = state.phoneNumber,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Gray2,
                    )
                }

                item {
                    PhotographerSection(
                        photographerName = state.photographerName,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                item {
                    ProductInfoSection(
                        photographerImageUrl = state.productImageUrl,
                        photographerName = state.productName,
                        price = state.price,
                        description = state.productDescription,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                item {
                    ScheduleSection(
                        shootingTime = state.shootingTime,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

            CommonBottomButton(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 48.dp),
                text = stringResource(R.string.order_detail_button_next),
                onClick = onNextClick,
            )
        }
    }
}

@Preview
@Composable
private fun OrderDetailScreenPreview() {
    OrderDetailScreenContent(
        state =
            OrderDetailState(
                orderNumber = "nnnnmmdd123456",
                orderTime = "2025-03-09 19:09:14",
                customerName = "주은강",
                phoneNumber = "01023293185",
                photographerName = "주문고",
                productName = "남친생기는 프사❤️",
                productImageUrl = "https://picsum.photos/id/222/300/300",
                price = 12000,
                productDescription = "작가가 해놓은 한줄소개 기렁지면 이렇게 처리 작가가 해놓은 한줄소개 기렁지면 이렇게 처리",
                shootingTime = "25.01.09 - 오후 02:00",
                isLoading = false,
            ),
        onBackClick = {},
        onNextClick = {},
    )
}
