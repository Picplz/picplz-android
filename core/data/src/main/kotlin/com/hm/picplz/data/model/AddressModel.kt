package com.hm.picplz.data.model

data class AreaData(
    val id: Long,
    val name: String,
    val dong: String,
    val ri: String?,
)

data class AreaSearchRequest(
    val keyword: String,
)

data class AreaNearbyRequest(
    val rad: Int,
    val lat: Double,
    val lng: Double,
)
