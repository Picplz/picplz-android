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
            private val DUMMY_PHOTOGRAPHERS =
                listOf(
                    Photographer(
                        id = 14,
                        name = "서연",
                        profileImageUri = "https://picsum.photos/id/64/200",
                        isActive = false,
                        distance = 80,
                        photoMoods = listOf("CASUAL", "SIMPLE"),
                        activeAreas = listOf("종로구 무악동", "서대문구 충정로"),
                    ),
                    Photographer(
                        id = 15,
                        name = "도윤",
                        profileImageUri = "https://picsum.photos/id/65/200",
                        isActive = false,
                        distance = 200,
                        photoMoods = listOf("VINTAGE", "HIP"),
                        activeAreas = listOf("종로구 교남동", "중구 을지로"),
                    ),
                    Photographer(
                        id = 16,
                        name = "유진",
                        profileImageUri = "https://picsum.photos/id/91/200",
                        isActive = false,
                        distance = 350,
                        photoMoods = listOf("DREAMY", "GORGEOUS"),
                        activeAreas = listOf("마포구 연남동", "마포구 합정동", "용산구 이태원동"),
                    ),
                )
        }
    }
