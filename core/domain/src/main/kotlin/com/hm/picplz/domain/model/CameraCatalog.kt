package com.hm.picplz.domain.model

data class CameraCatalog(
    val brands: List<CameraBrand>,
    val types: List<String>,
)

data class CameraBrand(
    val name: String,
    val models: List<String>,
)
