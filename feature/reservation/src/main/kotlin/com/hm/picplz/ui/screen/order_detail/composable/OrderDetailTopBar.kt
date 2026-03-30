package com.hm.picplz.ui.screen.order_detail.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hm.picplz.ui.screen.common.CommonTopBar

@Composable
fun OrderDetailTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonTopBar(
        modifier = modifier,
        text = "주문 상세",
        onClickBack = onBackClick,
    )
}
