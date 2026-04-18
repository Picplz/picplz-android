package com.hm.picplz.ui.screen.my_page

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.hm.picplz.ui.theme.PicplzTheme
import org.junit.Rule
import org.junit.Test

class FollowedPhotographersScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loading_state_exposes_accessible_description() {
        composeTestRule.setContent {
            PicplzTheme {
                FollowedPhotographersScreenContent(
                    state = FollowedPhotographersState(isLoading = true),
                    onIntent = {},
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("팔로우 작가를 불러오는 중이에요")
            .assertIsDisplayed()
    }

    @Test
    fun unavailable_state_shows_honest_unavailable_copy() {
        composeTestRule.setContent {
            PicplzTheme {
                FollowedPhotographersScreenContent(
                    state = FollowedPhotographersState(isLoading = false, isUnavailable = true),
                    onIntent = {},
                )
            }
        }

        composeTestRule.onNodeWithText("아직 준비 중인 기능이에요").assertIsDisplayed()
        composeTestRule.onNodeWithText("정식 지원 전까지는 팔로우한 작가를 여기서 확인할 수 없어요").assertIsDisplayed()
    }

    @Test
    fun empty_state_keeps_real_no_followed_photographers_message() {
        composeTestRule.setContent {
            PicplzTheme {
                FollowedPhotographersScreenContent(
                    state = FollowedPhotographersState(isLoading = false),
                    onIntent = {},
                )
            }
        }

        composeTestRule.onNodeWithText("아직 팔로우한 작가가 없어요").assertIsDisplayed()
        composeTestRule.onNodeWithText("마음에 드는 작가를 팔로우하면 이곳에서 모아볼 수 있어요").assertIsDisplayed()
    }
}
