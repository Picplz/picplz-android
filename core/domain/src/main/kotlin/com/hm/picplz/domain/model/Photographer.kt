package com.hm.picplz.domain.model

data class Photographer(
    val id: Long,
    val name: String,
    val profileImageUri: String?,
    val isActive: Boolean,
    val distance: Long,
    val photoMoods: List<String>,
)

data class FilteredPhotographers(
    val active: List<Photographer> = emptyList(),
    val inactive: List<Photographer> = emptyList(),
)
