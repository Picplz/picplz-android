package com.hm.picplz.ui.model

import com.hm.picplz.data.model.AreaData

data class SearchedArea(
    val id: Long,
    val name: String,
    val dong: String,
    val ri: String?
) {
    val displayName: String
        get() = ri?.let { "$dong $it" } ?: dong
}

fun AreaData.toUiModel(): SearchedArea {
    return SearchedArea(
        id = id,
        name = name,
        dong = dong,
        ri = ri
    )
}