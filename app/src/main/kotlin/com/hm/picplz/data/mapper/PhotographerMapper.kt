package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.PhotographerResponse
import com.hm.picplz.domain.model.Photographer

fun PhotographerResponse.toDomain(): Photographer {
    return Photographer(
        id = id,
        name = name,
        location = location,
        profileImageUri = profileImageUri,
        isActive = isActive,
        workingArea = workingArea,
        distance = distance,
        followers = followers,
        socialAccount = socialAccount ?: "",
        portfolioPhotos = portfolioPhotos,
    )
}

fun List<PhotographerResponse>.toDomain(): List<Photographer> {
    return map { it.toDomain() }
}
