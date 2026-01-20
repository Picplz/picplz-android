package com.hm.picplz.common.model

import java.util.UUID

enum class ChipMode {
    DEFAULT,
    ADD,
    EDIT
}

data class ChipItem(
    val id: String = UUID.randomUUID().toString(),
    val label: String,
    val initialMode: ChipMode = ChipMode.DEFAULT,
    val isEditable: Boolean = false,
)
