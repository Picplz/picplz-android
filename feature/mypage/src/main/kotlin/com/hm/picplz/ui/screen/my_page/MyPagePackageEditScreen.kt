package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyPagePackageEditRoute(
    viewModel: MyPagePackageEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is MyPagePackageEditSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    MyPagePackageEditScreen(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}

@Composable
fun MyPagePackageEditScreen(
    state: MyPagePackageEditState,
    onIntent: (MyPagePackageEditIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(
                title = stringResource(R.string.package_edit_title),
                onClickBack = { onIntent(MyPagePackageEditIntent.NavigateBack) },
            )
        },
        bottomBar = {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
            ) {
                CommonBottomButton(
                    text = stringResource(R.string.package_edit_save),
                    onClick = {},
                    enabled = state.isSaveEnabled,
                )
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
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MainThemeColor.White)
                    .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Text(
                text = stringResource(R.string.package_edit_placeholder_title),
                style = MainThemeFont.TitleSmall,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.package_edit_placeholder_description),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun MyPagePackageEditScreenPreview() {
    PicplzTheme {
        MyPagePackageEditScreen(
            state = MyPagePackageEditState.idle(),
            onIntent = {},
        )
    }
}
