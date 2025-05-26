package com.hm.picplz.data.service

import com.hm.picplz.data.model.AreaSearchRequest
import com.hm.picplz.data.source.AddressSource
import com.hm.picplz.ui.model.SearchedArea
import com.hm.picplz.ui.model.toUiModel
import javax.inject.Inject

interface AddressService {
    suspend fun searchArea(keyword: String): Result<List<SearchedArea>>
}

class AddressServiceImpl @Inject constructor(
    private val addressSource: AddressSource
) : AddressService {
    override suspend fun searchArea(keyword: String): Result<List<SearchedArea>> {
        return addressSource.searchArea(
            AreaSearchRequest(keyword = keyword)
        ).map { response ->
            response.data.map { areaData ->
                areaData.toUiModel()
            }
        }
    }
}