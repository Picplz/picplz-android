package com.hm.picplz.ui.model

import com.hm.picplz.data.model.AreaData

data class Area(
    val id: Long,
    val name: String,
    val dong: String,
    val ri: String?
) {
    val displayName: String
        get() = ri?.let { "$dong $it" } ?: dong
}

fun AreaData.toUiModel(): Area {
    return Area(
        id = id,
        name = name,
        dong = dong,
        ri = ri
    )
}