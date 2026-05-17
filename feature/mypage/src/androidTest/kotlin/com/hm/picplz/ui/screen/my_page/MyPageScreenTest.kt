package com.hm.picplz.ui.screen.my_page

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.theme.PicplzTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MyPageScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun photographerPreviewButtonIsDisabledWithoutPackage() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeRule.setContent {
            PicplzTheme {
                MyPageScreen(
                    state = photographerState(packagePreview = null),
                    onIntent = {},
                    navController = rememberNavController(),
                )
            }
        }

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_preview_profile))
            .assertIsNotEnabled()

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_empty_package))
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_empty_portfolio))
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_satisfaction_title))
            .assertIsDisplayed()
    }

    @Test
    fun photographerPreviewButtonIsEnabledWithPackage() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intents = mutableListOf<MyPageIntent>()

        composeRule.setContent {
            PicplzTheme {
                MyPageScreen(
                    state =
                        photographerState(
                            packagePreview =
                                PhotographerPackagePreview(
                                    imageUrl = "https://picsum.photos/seed/test-package/300/200",
                                    title = "남친생기는 프사",
                                    price = 66000,
                                    meta = "프로필 촬영 · 30분",
                                    description = "원본 20장과 보정본 3장을 제공해요.",
                                ),
                        ),
                    onIntent = { intents += it },
                    navController = rememberNavController(),
                )
            }
        }

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_preview_profile))
            .assertIsEnabled()
            .performClick()

        composeRule.runOnIdle {
            assertTrue(intents.contains(MyPageIntent.NavigateToPhotographerPreview))
        }

        composeRule.onNodeWithText("남친생기는 프사").assertIsDisplayed()

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_satisfaction_title))
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(context.getString(R.string.my_page_metric_portfolios))
            .assertCountEquals(0)
    }

    @Test
    fun photographerAuditSectionsRenderLockedProfileRowsAndEditLinks() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeRule.setContent {
            PicplzTheme {
                MyPageScreen(
                    state = photographerState(packagePreview = null),
                    onIntent = {},
                    navController = rememberNavController(),
                )
            }
        }

        composeRule
            .onAllNodesWithText(context.getString(R.string.my_page_metric_followers))
            .assertCountEquals(0)

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_region_edit))
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_keyword_edit))
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_equipment_edit))
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(context.getString(R.string.my_page_edit))
            .assertCountEquals(2)

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_portfolio_title))
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(context.getString(R.string.my_page_satisfaction_title))
            .assertIsDisplayed()
    }

    @Test
    fun packageEditEmptyListShowsAddPackageCard() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeRule.setContent {
            PicplzTheme {
                MyPagePackageEditScreen(
                    state = MyPagePackageEditState.idle(),
                    onIntent = {},
                )
            }
        }

        composeRule
            .onNodeWithText(context.getString(R.string.package_edit_add_package))
            .assertIsDisplayed()
    }

    private fun photographerState(packagePreview: PhotographerPackagePreview?): MyPageState {
        return MyPageState(
            nickname = "임두현",
            hasPhotographerRole = true,
            photographerProfile =
                PhotographerProfile(
                    photographerId = 1,
                    displayName = "유가영 작가",
                    followerCount = 128,
                    packageCount = if (packagePreview == null) 0 else 1,
                    portfolioCount = 0,
                    instagramId = "@gayoung.photo",
                    isInstagramRegistered = true,
                    introduction = "감도 높은 자연광 프로필 촬영을 진행해요.",
                    regionSummary = "서울 마포구, 서울 용산구 외 16개 지역",
                    keywordSummary = "#캐주얼, #심플, #공주감성 외 3개 키워드",
                    equipmentSummary = "아이폰 16 PRO, 아이폰 X 외 3개 장비",
                    hasPackages = packagePreview != null,
                    packagePreview = packagePreview,
                    satisfactionSummary =
                        PhotographerSatisfactionSummary(
                            averageRating = "4.9",
                            reviewCount = 48,
                            repeatBookingRate = 82,
                        ),
                ),
        )
    }
}
