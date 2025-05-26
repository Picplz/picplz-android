package com.hm.picplz.data.source

import com.hm.picplz.data.api.AddressApi
import com.hm.picplz.data.model.AreaSearchRequest
import com.hm.picplz.data.model.AreaSearchResponse
import javax.inject.Inject

interface  AddressSource {
    suspend fun searchArea(request: AreaSearchRequest): Result<AreaSearchResponse>
}

class AddressSourceImpl @Inject constructor(
    private val addressApi: AddressApi
) : AddressSource {
    override suspend fun searchArea(request: AreaSearchRequest): Result<AreaSearchResponse> =
        runCatching {
            addressApi.searchAreas(request.keyword)
        }
}