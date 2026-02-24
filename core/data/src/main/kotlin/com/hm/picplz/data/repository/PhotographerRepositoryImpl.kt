package com.hm.picplz.data.repository

import android.util.Log
import com.hm.picplz.data.service.PhotographerService
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.repository.PhotographerRepository
import javax.inject.Inject

class PhotographerRepositoryImpl
    @Inject
    constructor(
        private val photographerService: PhotographerService,
    ) : PhotographerRepository {
        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): Result<FilteredPhotographers> {
            return photographerService.getNearbyPhotographers(longitude, latitude, distance)
                .recoverCatching { error ->
                    // TODO: 백엔드 GET /members/location/nearby 500 에러 수정 후 이 폴백 제거
                    Log.w("PhotographerRepo", "API 실패, 더미 데이터 반환", error)
                    FilteredPhotographers(
                        active = DUMMY_PHOTOGRAPHERS.filter { it.isActive },
                        inactive = DUMMY_PHOTOGRAPHERS.filter { !it.isActive },
                    )
                }
        }

        companion object {
            // TODO: 백엔드 수정 후 삭제
            @Suppress("MagicNumber")
            private val DUMMY_PHOTOGRAPHERS =
                listOf(
                    Photographer(
                        id = 14,
                        name = "유가영 작가",
                        profileImageUri = "https://picsum.photos/id/64/200",
                        isActive = true,
                        distance = 80,
                        photoMoods = listOf("을지로 감성", "MZ 감성"),
                        activeAreas = listOf("마포구", "구로구", "노원구", "강남구", "서초구", "용산구"),
                    ),
                    Photographer(
                        id = 15,
                        name = "김도윤 작가",
                        profileImageUri = "https://picsum.photos/id/65/200",
                        isActive = true,
                        distance = 200,
                        photoMoods = listOf("빈티지", "힙한"),
                        activeAreas = listOf("종로구", "중구"),
                    ),
                    Photographer(
                        id = 16,
                        name = "박서준 작가",
                        profileImageUri = "https://picsum.photos/id/91/200",
                        isActive = true,
                        distance = 150,
                        photoMoods = listOf("키치 감성", "퇴폐 감성", "MZ 감성"),
                        activeAreas = listOf("마포구", "합정동", "이태원동"),
                    ),
                    Photographer(
                        id = 17,
                        name = "이하은 작가",
                        profileImageUri = "https://picsum.photos/id/177/200",
                        isActive = false,
                        distance = 300,
                        photoMoods = listOf("드리미", "감성적인"),
                        activeAreas = listOf("서대문구", "은평구"),
                    ),
                    Photographer(
                        id = 18,
                        name = "정민수 작가",
                        profileImageUri = "https://picsum.photos/id/180/200",
                        isActive = false,
                        distance = 450,
                        photoMoods = listOf("캐주얼", "심플"),
                        activeAreas = listOf("강북구", "도봉구", "성북구", "동대문구"),
                    ),
                    Photographer(
                        id = 19,
                        name = "최유진 작가",
                        profileImageUri = "https://picsum.photos/id/203/200",
                        isActive = false,
                        distance = 500,
                        photoMoods = listOf("화려한", "고급스러운"),
                        activeAreas = listOf("송파구", "강동구"),
                    ),
                )
        }
    }
