package com.hm.picplz.ui.screen.my_page

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.screen.common.KakaoMapView
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.kakao.vectormap.LatLng
import java.text.NumberFormat
import java.util.Locale
import com.hm.picplz.feature.mypage.R as MypageR

private fun formatPrice(amount: Int): String {
    return NumberFormat.getNumberInstance(Locale.KOREA).format(amount)
}

@Composable
fun MyPageOrderSheetScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: OrderSheetViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is OrderSheetSideEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is OrderSheetSideEffect.ShowReceiptUnavailable -> {
                    Toast.makeText(
                        context,
                        context.getString(MypageR.string.order_detail_receipt_unavailable),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    val orderInfo = state.orderInfo
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(
                title = stringResource(MypageR.string.order_detail_title),
            ) {
                viewModel.handleIntent(OrderSheetIntent.NavigateBack)
            }
        },
        modifier =
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .verticalScroll(scrollState),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 15.dp),
            ) {
                OrderNumberSection(orderInfo)

                Spacer(modifier = Modifier.height(30.dp))

                CustomerInfoSection(orderInfo)

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)
                Spacer(modifier = Modifier.height(20.dp))

                PhotographerSection(orderInfo)

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)
                Spacer(modifier = Modifier.height(20.dp))

                ProductInfoSection(orderInfo)

                Spacer(modifier = Modifier.height(30.dp))

                ShootingTimeSection(orderInfo)

                Spacer(modifier = Modifier.height(30.dp))

                ShootingLocationSection(orderInfo)

                Spacer(modifier = Modifier.height(24.dp))
            }

            HorizontalDivider(thickness = 10.dp, color = MainThemeColor.Gray1)

            Column(
                modifier = Modifier.padding(horizontal = 15.dp),
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                PaymentSection(
                    orderInfo = orderInfo,
                    onReceiptClick = {
                        viewModel.handleIntent(OrderSheetIntent.ViewReceipt)
                    },
                )

                Spacer(modifier = Modifier.height(30.dp))
            }

            FooterSection()
        }
    }
}

@Composable
private fun OrderNumberSection(orderInfo: OrderInfo) {
    Text(
        text = stringResource(MypageR.string.order_detail_order_number),
        style = MainThemeFont.ButtonDefault,
    )
    Spacer(modifier = Modifier.height(4.dp))
    Column {
        Text(
            text = orderInfo.orderNum,
            style = MainThemeFont.BodyBold,
            color = Color(0xFFEF4747),
        )
        Text(
            text = orderInfo.orderDate,
            style = MainThemeFont.BodyBold,
            color = MainThemeColor.Gray4,
        )
    }
}

@Composable
private fun CustomerInfoSection(orderInfo: OrderInfo) {
    Text(
        text = stringResource(MypageR.string.order_detail_customer_info),
        style = MainThemeFont.ButtonDefault,
    )
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(MypageR.string.order_detail_name_label),
                style = MainThemeFont.InnerTag,
                color = MainThemeColor.Gray4,
            )
            Text(text = orderInfo.clientName, style = MainThemeFont.Body)
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(MypageR.string.order_detail_phone_label),
                style = MainThemeFont.InnerTag,
                color = MainThemeColor.Gray4,
            )
            Text(text = orderInfo.clientPhoneNumber, style = MainThemeFont.Body)
        }
    }
}

@Composable
private fun PhotographerSection(orderInfo: OrderInfo) {
    Text(
        text = stringResource(MypageR.string.order_detail_photographer_name),
        style = MainThemeFont.ButtonDefault,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = orderInfo.photographerName,
        style = MainThemeFont.Body,
        color = MainThemeColor.Gray5,
    )
}

@Composable
private fun ProductInfoSection(orderInfo: OrderInfo) {
    Text(
        text = stringResource(MypageR.string.order_detail_product_info),
        style = MainThemeFont.ButtonDefault,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .border(1.dp, MainThemeColor.Black, RoundedCornerShape(5.dp))
                .padding(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Image(
                painter = painterResource(id = orderInfo.productImage),
                contentDescription = stringResource(MypageR.string.order_detail_package_image),
                modifier =
                    Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(5.dp)),
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(70.dp),
            ) {
                Column {
                    Text(
                        text = orderInfo.productName,
                        style = MainThemeFont.TitleSmall,
                    )
                    Text(
                        text =
                            stringResource(
                                MypageR.string.order_detail_price_symbol_format,
                                formatPrice(orderInfo.productCost),
                            ),
                        style = MainThemeFont.Body,
                    )
                }
                Text(
                    text = orderInfo.productDescription,
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray4,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ShootingTimeSection(orderInfo: OrderInfo) {
    Text(
        text = stringResource(MypageR.string.order_detail_shooting_time),
        style = MainThemeFont.ButtonDefault,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = orderInfo.shootingTime,
        style = MainThemeFont.Body,
        color = MainThemeColor.Gray5,
    )
}

@Composable
private fun ShootingLocationSection(orderInfo: OrderInfo) {
    Text(
        text = stringResource(MypageR.string.order_detail_shooting_location),
        style = MainThemeFont.ButtonDefault,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = orderInfo.shootingLocation,
        style = MainThemeFont.Body,
        color = MainThemeColor.Gray5,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Box(
        modifier =
            Modifier
                .height(95.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        val position =
            LatLng.from(
                orderInfo.shootingLatitude,
                orderInfo.shootingLongitude,
            )
        KakaoMapView(
            modifier = Modifier.fillMaxSize(),
            initialPosition = position,
            isGestureEnabled = false,
        )
        Image(
            painter = painterResource(id = R.drawable.ic_order_marker),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
        )
    }
}

@Composable
private fun PaymentSection(
    orderInfo: OrderInfo,
    onReceiptClick: () -> Unit,
) {
    Text(
        text = stringResource(MypageR.string.order_detail_final_payment_header),
        style = MainThemeFont.ButtonDefault,
    )
    Spacer(modifier = Modifier.height(12.dp))

    HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Black)
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(MypageR.string.order_detail_product_price),
            style = MainThemeFont.BodyBold,
            color = MainThemeColor.Gray4,
        )
        Text(
            text =
                stringResource(
                    MypageR.string.order_detail_price_won_format,
                    formatPrice(orderInfo.productCost),
                ),
            style = MainThemeFont.BodyLarge,
            color = MainThemeColor.Gray4,
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(MypageR.string.order_detail_discount),
            style = MainThemeFont.BodyBold,
            color = MainThemeColor.Gray4,
        )
        Text(
            text =
                stringResource(
                    MypageR.string.order_detail_price_won_format,
                    formatPrice(orderInfo.discountCost),
                ),
            style = MainThemeFont.BodyLarge,
            color = MainThemeColor.Gray4,
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Black)
    Spacer(modifier = Modifier.height(12.dp))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(MypageR.string.order_detail_total_payment),
            style = MainThemeFont.BodyBold,
        )
        Text(
            text =
                stringResource(
                    MypageR.string.order_detail_price_won_format,
                    formatPrice(orderInfo.totalCost),
                ),
            style = MainThemeFont.BodyLarge,
        )
    }
    Spacer(modifier = Modifier.height(10.dp))

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        PaymentCardBox(orderInfo)

        OutlinedButton(
            onClick = onReceiptClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, MainThemeColor.Gray3),
            colors =
                ButtonDefaults.outlinedButtonColors(
                    containerColor = MainThemeColor.White,
                    contentColor = MainThemeColor.Gray6,
                ),
            contentPadding = PaddingValues(10.dp),
        ) {
            Text(
                text = stringResource(MypageR.string.order_detail_receipt),
                style = MainThemeFont.ButtonDefault,
            )
        }
    }
}

@Composable
private fun PaymentCardBox(orderInfo: OrderInfo) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .border(1.dp, MainThemeColor.Gray3, RoundedCornerShape(5.dp))
                .background(MainThemeColor.Gray1, RoundedCornerShape(5.dp))
                .padding(horizontal = 17.dp, vertical = 14.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(MypageR.string.order_detail_credit_card),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray5,
                )
                Text(
                    text =
                        stringResource(
                            MypageR.string.order_detail_price_won_format,
                            formatPrice(orderInfo.totalCost),
                        ),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray4,
                )
            }
            Row {
                Text(
                    text = orderInfo.cardName,
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray5,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(MypageR.string.order_detail_installment),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray5,
                )
            }
            Text(
                text = orderInfo.paymentDate,
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray3,
            )
        }
    }
}

@Composable
private fun FooterSection() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_footer_logo),
            contentDescription = stringResource(MypageR.string.footer_logo),
            modifier =
                Modifier
                    .height(53.dp)
                    .width(50.dp),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(MypageR.string.footer_company_info),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
            Text(
                text = stringResource(MypageR.string.footer_address),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
            Text(
                text = stringResource(MypageR.string.footer_business_number),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
            Text(
                text = stringResource(MypageR.string.footer_commerce_registration),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
            Text(
                text = stringResource(MypageR.string.footer_privacy_officer),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
            Text(
                text = stringResource(MypageR.string.footer_email),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
            Text(
                text = stringResource(MypageR.string.footer_customer_center),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
        }
    }
}
