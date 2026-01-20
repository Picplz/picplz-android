package com.hm.picplz.data.source

import com.hm.picplz.data.model.PhotographerListResponse
import com.hm.picplz.data.provider.DummyPhotographerProvider
import javax.inject.Inject

interface PhotographerSource {
    suspend fun getPhotographers(): Result<PhotographerListResponse>
}

class PhotographerSourceImpl
    @Inject
    constructor(
//    private val photographerApi: PhotographerApi,
        private val photographerApi: DummyPhotographerProvider,
    ) : PhotographerSource {
        override suspend fun getPhotographers(): Result<PhotographerListResponse> = runCatching { photographerApi.getPhotographers() }
    }
