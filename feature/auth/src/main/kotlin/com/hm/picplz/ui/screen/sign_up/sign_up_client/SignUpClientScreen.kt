package com.hm.picplz.ui.screen.sign_up.sign_up_client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hm.picplz.common.mockdata.emptyUserData
import com.hm.picplz.common.model.User
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpClientScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    @Suppress("UNUSED_PARAMETER") _userInfo: User = emptyUserData,
    viewModel: SignUpClientViewModel = viewModel(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CommonTopBar(
                text = "고객 선택",
                onClickBack = {},
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpClientSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
            }
        }
    }
}
