package com.hm.picplz.data.model

import com.google.gson.annotations.SerializedName

data class UploadUrlResponseDto(
    @SerializedName("uploadUrl") val uploadUrl: String,
    @SerializedName("objectKey") val objectKey: String,
)
