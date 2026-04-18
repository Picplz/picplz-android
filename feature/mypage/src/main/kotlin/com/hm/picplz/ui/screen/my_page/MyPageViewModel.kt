package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                    sendSideEffect(
                        MyPageSideEffect.ShowToast("내 리뷰 기능은 준비 중입니다."),
                    )
                }
                is MyPageIntent.NavigateToTerms -> {
                    sendSideEffect(
                        MyPageSideEffect.ShowToast("이용 약관 기능은 준비 중입니다."),
                    )
                }
                is MyPageIntent.SwitchToPhotographer -> {
                    // DEV: 토글로 모드 전환 확인용
                    _state.update { it.copy(hasPhotographerRole = !it.hasPhotographerRole) }
                }
                is MyPageIntent.NavigateToPhotographerSignUp -> {
                    _state.update { it.copy(hasPhotographerRole = true) }
                }
                is MyPageIntent.DevToggleShootings -> {
                    _state.update {
                        if (it.ongoingShootings.isEmpty()) {
                            it.copy(ongoingShootings = DEV_MOCK_SHOOTINGS)
                        } else {
                            it.copy(ongoingShootings = emptyList())
                        }
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
                )
        }
    }
