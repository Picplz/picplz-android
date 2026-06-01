package com.hm.picplz.data.model

import com.google.gson.JsonElement

data class ApiResponse<T>(
    val timestamp: String,
    val statusCode: Int,
    val message: String,
    val data: T,
)

data class ApiErrorResponseDto(
    val timestamp: String?,
    val statusCode: Int?,
    val message: String?,
    val data: JsonElement?,
)
