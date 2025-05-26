package com.hm.picplz.ui.model

import com.hm.picplz.data.model.AreaData

data class SearchedArea(
    val id: Int,
    val fullName: String,
    val dong: String,
    val ri: String?
) {
    val displayName: String
        get() = ri?.let { "$dong $it" } ?: dong
}

fun AreaData.toUiModel(): SearchedArea {
    return SearchedArea(
        id = id,
        fullName = name,
        dong = dong,
        ri = ri
    )
}