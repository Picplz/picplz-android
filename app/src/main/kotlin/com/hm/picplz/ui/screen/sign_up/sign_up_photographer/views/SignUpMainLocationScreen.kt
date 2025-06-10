package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views

import CommonOutlinedTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.common.CommonSearchField
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.hm.picplz.R
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.composable.AreaListItem
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun SignUpMainLocationScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = hiltViewModel(),
    mainNavController: NavHostController,
    signUpPhotographerNavController: NavController,
) {
    val currentState by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = MainThemeColor.White
    ){
        innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            CommonTopBar(
                text = "주 촬영지",
                onClickBack = {viewModel.handleIntent(NavigateToPrev)}
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    text = "주 촬영지(동)를 선택해 주세요.",
                    style = MaterialTheme.typography.titleMedium
                )
                CommonSearchField(
                    modifier = Modifier.padding(top = 30.dp),
                    value = currentState.searchQuery,
                    onValueChange = { searchText ->
                        viewModel.handleIntent(
                            SignUpPhotographerIntent.UpdateSearchQuery(searchText)
                        )
                    },
                    placeholder = "동명(동, 면)으로 검색 (ex, 연남동)",
                    onSearchClick = {
                        viewModel.handleIntent(
                            SignUpPhotographerIntent.SearchArea(
                                currentState.searchQuery
                            )
                        )
                    },
                    keyboardActions = {
                        viewModel.handleIntent(
                            SignUpPhotographerIntent.SearchArea(
                                currentState.searchQuery
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(30.dp))
                when {
                    currentState.isSearching -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    currentState.searchResults.isNotEmpty() -> {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.marker_icon),
                                    contentDescription = "아이콘",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = when {
                                        currentState.searchQuery.isBlank() -> "근처 동네"
                                        else -> "'${currentState.searchQuery}' 검색 결과"
                                    },
                                    style = MainFontFamily.buttonDefault,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MainThemeColor.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            LazyColumn(
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                items(currentState.searchResults) { area ->
                                    AreaListItem(
                                        area = area,
                                        onItemClick = {}
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    mainNavController.popBackStack()
                }
                else -> {}
            }
        }
    }
}



@Preview (showBackground = true)
@Composable
fun SignUpMainLocationScreenPreview() {
    PicplzTheme {
        val mainNavController = rememberNavController()
        val signUpPhotographerNavController = rememberNavController()

        SignUpMainLocationScreen(
            mainNavController = mainNavController,
            signUpPhotographerNavController = signUpPhotographerNavController
        )
    }
}