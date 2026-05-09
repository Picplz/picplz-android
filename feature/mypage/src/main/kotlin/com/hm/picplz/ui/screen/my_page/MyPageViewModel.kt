package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.feature.mypage.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state = MutableStateFlow(DEV_MOCK_STATE)
        val state: StateFlow<MyPageState> get() = _state

        private val _sideEffect = Channel<MyPageSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: MyPageIntent) {
            when (intent) {
                is MyPageIntent.NavigateToModifyProfile -> {
                    sendSideEffect(MyPageSideEffect.NavigateToModifyProfile)
                }
                is MyPageIntent.NavigateToPhotographerModifyProfile -> {
                    sendSideEffect(MyPageSideEffect.NavigateToPhotographerModifyProfile)
                }
                is MyPageIntent.NavigateToShootingHistory -> {
                    sendSideEffect(MyPageSideEffect.NavigateToShootingHistory)
                }
                is MyPageIntent.NavigateToSettings -> {
                    sendSideEffect(MyPageSideEffect.NavigateToSettings)
                }
                is MyPageIntent.NavigateToFollowedPhotographers -> {
                    sendSideEffect(MyPageSideEffect.NavigateToFollowedPhotographers)
                }
                is MyPageIntent.NavigateToMyReviews -> {
                    sendSideEffect(MyPageSideEffect.NavigateToMyReviews)
                }
                is MyPageIntent.NavigateToTerms -> {
                    sendSideEffect(
                        MyPageSideEffect.ShowToast(R.string.my_page_terms_pending),
                    )
                }
                is MyPageIntent.ToggleUserMode -> {
                    _state.update { it.copy(hasPhotographerRole = !it.hasPhotographerRole) }
                }
                is MyPageIntent.NavigateToPhotographerSignUp -> {
                    sendSideEffect(MyPageSideEffect.NavigateToPhotographerSignUp)
                }
                is MyPageIntent.NavigateToPhotographerPreview -> {
                    val photographerProfile = _state.value.photographerProfile
                    if (photographerProfile.canPreviewProfile) {
                        sendSideEffect(
                            MyPageSideEffect.NavigateToPhotographerPreview(
                                photographerProfile.photographerId,
                            ),
                        )
                    } else {
                        sendSideEffect(MyPageSideEffect.ShowToast(R.string.my_page_preview_requires_package))
                    }
                }
                is MyPageIntent.NavigateToPhotographerRegionEdit -> {
                    sendSideEffect(
                        MyPageSideEffect.NavigateToPhotographerActiveAreaEdit(
                            _state.value.photographerProfile.photographerId,
                        ),
                    )
                }
                is MyPageIntent.NavigateToPhotographerKeywordEdit -> {
                    sendSideEffect(
                        MyPageSideEffect.NavigateToPhotographerKeywordEdit(
                            _state.value.photographerProfile.photographerId,
                        ),
                    )
                }
                is MyPageIntent.NavigateToPhotographerEquipmentEdit -> {
                    sendSideEffect(MyPageSideEffect.ShowToast(R.string.my_page_equipment_edit_pending))
                }
                is MyPageIntent.NavigateToSettlement -> {
                    sendSideEffect(MyPageSideEffect.ShowToast(R.string.my_page_settlement_pending))
                }
                is MyPageIntent.NavigateToPackageEdit -> {
                    sendSideEffect(MyPageSideEffect.NavigateToPackageEdit)
                }
                is MyPageIntent.NavigateToPortfolioEdit -> {
                    sendSideEffect(MyPageSideEffect.ShowToast(R.string.my_page_portfolio_edit_pending))
                }
                is MyPageIntent.ApplyPhotographerKeywordSummary -> {
                    _state.update {
                        it.copy(
                            photographerProfile =
                                it.photographerProfile.copy(
                                    keywordSummary = intent.keywordSummary,
                                ),
                        )
                    }
                }
                is MyPageIntent.ApplyDevPhotographerPreview -> {
                    _state.update {
                        it.copy(
                            ongoingShootings = if (intent.hasShootings) DEV_MOCK_SHOOTINGS else emptyList(),
                            photographerProfile =
                                it.photographerProfile.copy(
                                    packageCount = if (intent.hasPackagePreview) 1 else 0,
                                    portfolioCount =
                                        if (intent.hasPortfolioPreview) {
                                            DEV_MOCK_PORTFOLIO_URLS.size
                                        } else {
                                            0
                                        },
                                    hasPackages = intent.hasPackagePreview,
                                    packagePreview = if (intent.hasPackagePreview) DEV_MOCK_PACKAGE else null,
                                    portfolioPreviewImageUrls =
                                        if (intent.hasPortfolioPreview) {
                                            DEV_MOCK_PORTFOLIO_URLS
                                        } else {
                                            emptyList()
                                        },
                                ),
                        )
                    }
                }
            }
        }

        private fun sendSideEffect(effect: MyPageSideEffect) {
            viewModelScope.launch {
                _sideEffect.send(effect)
            }
        }

        companion object {
            private val DEV_MOCK_SHOOTINGS =
                listOf(
                    OngoingShootingItem(
                        photographerName = "합정동 불주먹",
                        photographerImageUri = "",
                        status = "예약 확정",
                        packageName = "남친생기는 프사",
                        shootingDate = "25.19.23",
                        shootingLocation = "종로구 효자로 33",
                    ),
                    OngoingShootingItem(
                        photographerName = "성수동 감성작가",
                        photographerImageUri = "",
                        status = "촬영 대기",
                        packageName = "인스타 피드촬영",
                        shootingDate = "25.04.15",
                        shootingLocation = "성동구 서울숲길 17",
                    ),
                )

            private val DEV_MOCK_STATE =
                MyPageState(
                    nickname = "임두현",
                    profileImageUri = "",
                    hasPhotographerRole = false,
                    ongoingShootings = emptyList(),
                    photographerProfile =
                        PhotographerProfile(
                            photographerId = 1,
                            displayName = "유가영 작가",
                            profileImageUri = "",
                            followerCount = 128,
                            packageCount = 0,
                            portfolioCount = 0,
                            instagramId = "imdooring",
                            isInstagramRegistered = true,
                            introduction = "안녕하세요, 유가영 작가입니다.",
                            regionSummary = "서울 마포구, 서울 용산구 외 16개 지역",
                            keywordSummary = "#캐주얼, #심플, #공주감성 외 3개 키워드",
                            equipmentSummary = "아이폰 16 PRO, 아이폰 X 외 3개 장비",
                            hasPackages = false,
                            packagePreview = null,
                            portfolioPreviewImageUrls = emptyList(),
                            satisfactionSummary =
                                PhotographerSatisfactionSummary(
                                    averageRating = "4.9",
                                    reviewCount = 48,
                                    repeatBookingRate = 82,
                                ),
                        ),
                )

            private val DEV_MOCK_PACKAGE =
                PhotographerPackagePreview(
                    imageUrl =
                        "https://images.unsplash.com/photo-1513279922550-250c2129b13a?auto=format&fit=crop&w=1200&q=80",
                    title = "남친 생기는 프사❤️",
                    price = 9900,
                    meta = "15분 이내",
                    description =
                        "여자친구 /남자친구 생기는 카톡프사 찍어드립니다~ 요즘 인스타그램 감성으로 이쁘게!\n" +
                            "사용기기: 아이폰 X / 아이폰 16pro\n" +
                            "베스트컷 5개정도 같이 뽑아드려용!",
                )

            private val DEV_MOCK_PORTFOLIO_URLS =
                listOf(
                    "https://images.unsplash.com/photo-1490750967868-88aa4486c946?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1468327768560-75b778cbb551?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1496062031456-07b8f162a322?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1501004318641-b39e6451bec6?auto=format&fit=crop&w=800&q=80",
                )
        }
    }
