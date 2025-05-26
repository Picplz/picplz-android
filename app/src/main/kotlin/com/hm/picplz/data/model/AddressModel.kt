package com.hm.picplz.data.model

data class AreaSearchRequest(
    val keyword: String,
)

data class AreaSearchResponse(
    val timeStamp: String,
    val statusCode: Int,
    val message: String,
    val data: List<AreaData>
)

data class AreaData(
    val id: Long,
    val name: String,
    val dong: String,
    val ri: String?,
)