package com.hm.picplz.domain.model

import com.kakao.vectormap.LatLng

data class Photographer(
    val id: Int,
    val name: String,
    val location: LatLng? = null,
    val profileImageUri: String,
    val isActive: Boolean,
    val workingArea: String,
    val distance: Number,
    val followers: List<Number>,
    val socialAccount: String,
    val portfolioPhotos: List<String>,
)

data class FilteredPhotographers(
    val active: List<Photographer> = emptyList(),
    val inactive: List<Photographer> = emptyList()
)
