package com.hm.picplz.ui.screen.chat

import CommonStatusTag
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.navigation.bottom_navigation.BottomNavigationBar
import com.hm.picplz.ui.model.ChatStatus
import com.hm.picplz.ui.screen.chat.composable.ChatRoomList
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.buttonText
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val currentState = viewModel.state.collectAsState().value
    val tabs = ChatTabType.entries.toTypedArray()
    val selectedTabIndex = tabs.indexOf(currentState.selectedTab).takeIf { it >= 0 } ?: 0


    val filteredRooms = dummyChatRooms.filter {
        it.chatStatus in currentState.currentStatusTags
    }

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "채팅",
                    style = pretendardTypography.titleMedium,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
            )
        },
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MainThemeColor.White,
                    contentColor = MainThemeColor.Black,
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            Box(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                    .height(2.dp)
                                    .background(
                                        color = MainThemeColor.Black,
                                        shape = RoundedCornerShape(percent = 50)
                                    )
                            )
                        }
                    },
                    divider = {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MainThemeColor.Gray3
                        )
                    },
                ) {
                  tabs.forEach {tabType ->
                      val tabText = when (tabType) {
                        ChatTabType.ONGOING -> "진행중"
                        ChatTabType.COMPLETED -> "완료됨"
                      }
                      val isSelected = tabType == currentState.selectedTab
                      Tab(
                          selected = isSelected,
                          onClick = {
                              viewModel.handleIntent(
                                  ChatIntent.SetSelectedTab(tabType)
                              )
                          },
                          text = {
                              Text(
                                  text = tabText,
                                  style = if (isSelected) buttonText else pretendardTypography.bodyLarge,
                                  color = if (isSelected) MainThemeColor.Black else MainThemeColor.Gray3,
                              )
                          }
                      )
                  }
                }
            }
            LazyRow(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 4.dp
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(currentState.currentStatusTags) { statusTag ->
                    CommonStatusTag(
                        label = when (statusTag) {
                            ChatStatus.PENDING -> "예약 대기"
                            ChatStatus.CONFIRMED -> "예약 확정"
                            ChatStatus.REJECTED -> "촬영 거절"
                            ChatStatus.COMPLETED -> "촬영 완료"
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))


            ChatRoomList(
                chatRooms = filteredRooms,
                chatTabType = currentState.selectedTab,
                navController = navController
            )
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        ChatScreen(
            navController = navController
        )
    }
}
