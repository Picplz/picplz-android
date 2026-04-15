package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.feature.mypage.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowedPhotographersViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state = MutableStateFlow(FollowedPhotographersState.idle())
        val state: StateFlow<FollowedPhotographersState> = _state

        private val _sideEffect = Channel<FollowedPhotographersSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            loadFollowedPhotographers()
        }

        fun handleIntent(intent: FollowedPhotographersIntent) {
            when (intent) {
                is FollowedPhotographersIntent.NavigateBack -> {
                    sendSideEffect(FollowedPhotographersSideEffect.NavigateBack)
                }

                is FollowedPhotographersIntent.SelectPhotographer -> {
                    sendSideEffect(
                        FollowedPhotographersSideEffect.NavigateToPhotographerDetail(intent.photographerId),
                    )
                }
            }
        }

        private fun loadFollowedPhotographers() {
            viewModelScope.launch {
                delay(350)
                _state.value = loadedState(isDebugBuild = BuildConfig.DEBUG)
            }
        }

        private fun sendSideEffect(effect: FollowedPhotographersSideEffect) {
            viewModelScope.launch {
                _sideEffect.send(effect)
            }
        }

        companion object {
            internal fun loadedState(isDebugBuild: Boolean): FollowedPhotographersState {
                return if (isDebugBuild) {
                    FollowedPhotographersState(
                        isLoading = false,
                        photographers = FOLLOWED_PHOTOGRAPHERS,
                    )
                } else {
                    FollowedPhotographersState(
                        isLoading = false,
                        isUnavailable = true,
                    )
                }
            }

            private val FOLLOWED_PHOTOGRAPHERS =
                listOf(
                    FollowedPhotographerItem(
                        id = 1,
                        name = "유가영 작가",
                        profileImageUri = "https://picsum.photos/seed/followed-photographer-1/160/160",
                        workingAreas = listOf("마포구", "서대문구"),
                        keywords =
                            listOf(
                                "#을지로 감성",
                                "#MZ 감성",
                                "#필름톤",
                                "#스냅촬영",
                                "#프로필촬영",
                                "#감성보정",
                                "#야외촬영",
                                "#데이트스냅",
                            ),
                        isBookable = true,
                    ),
                    FollowedPhotographerItem(
                        id = 2,
                        name = "유가영 작가",
                        profileImageUri = "https://picsum.photos/seed/followed-photographer-2/160/160",
                        workingAreas = listOf("동작구", "영등포구"),
                        keywords = listOf("#을지로 감성", "#MZ 감성", "#자연광"),
                        isBookable = false,
                    ),
                    FollowedPhotographerItem(
                        id = 3,
                        name = "유가영 작가",
                        profileImageUri = "",
                        workingAreas = listOf("마포구", "망원구"),
                        keywords = listOf("#을지로 감성", "#MZ 감성", "#프로필맛집"),
                        isBookable = false,
                    ),
                    FollowedPhotographerItem(
                        id = 4,
                        name = "유가영 작가",
                        profileImageUri = "https://picsum.photos/seed/followed-photographer-4/160/160",
                        workingAreas = listOf("강남구", "강북구", "동대문구", "송파구", "성동구", "광진구"),
                        keywords = listOf("#을지로 감성", "#MZ 감성", "#하이틴"),
                        isBookable = false,
                    ),
                    FollowedPhotographerItem(
                        id = 5,
                        name = "유가영 작가",
                        profileImageUri = "https://picsum.photos/seed/followed-photographer-5/160/160",
                        workingAreas = listOf("강동구"),
                        keywords = listOf("#을지로 감성", "#MZ 감성", "#데이트스냅"),
                        isBookable = false,
                    ),
                )
        }
    }
