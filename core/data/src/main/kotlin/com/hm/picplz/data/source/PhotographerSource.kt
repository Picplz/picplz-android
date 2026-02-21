package com.hm.picplz.data.source

import com.hm.picplz.data.api.PhotographerApi
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.PhotographerListResponse
import com.hm.picplz.data.provider.DummyPhotographerProvider
import javax.inject.Inject

interface PhotographerSource {
    suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit>

    suspend fun getPhotographers(): Result<PhotographerListResponse>
}

class PhotographerSourceImpl
    @Inject
    constructor(
        private val photographerApi: PhotographerApi,
        private val dummyPhotographerProvider: DummyPhotographerProvider,
    ) : PhotographerSource {
        override suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit> =
            runCatching {
                val response = photographerApi.createPhotographer(request)
                if (response.isSuccessful) {
                    Unit
                } else {
                    error("Create photographer failed: ${response.code()} ${response.errorBody()?.string()}")
                }
            }

        override suspend fun getPhotographers(): Result<PhotographerListResponse> =
            runCatching { dummyPhotographerProvider.getPhotographers() }
    }
