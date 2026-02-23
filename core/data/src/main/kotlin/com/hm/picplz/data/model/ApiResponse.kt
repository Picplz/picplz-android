package com.hm.picplz.data.model

data class ApiResponse<T>(
    val timestamp: String,
    val statusCode: Int,
    val message: String,
    val data: T,
)
