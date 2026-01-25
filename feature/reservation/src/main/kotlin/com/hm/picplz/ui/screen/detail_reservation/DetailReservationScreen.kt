package com.hm.picplz.ui.screen.detail_reservation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DetailReservationScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = "예약 상세 화면",
        )
    }
}

@Preview
@Composable
fun DetailReservationScreenPreview() {
    DetailReservationScreen()
}
