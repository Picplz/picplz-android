package com.hm.picplz.domain.model

data class Area(
    val id: Long,
    val name: String,
    val dong: String,
    val ri: String?
) {
    val displayName: String
        get() = ri?.let { "$dong $it" } ?: dong
}
