package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.domain.model.Photographer

fun NearbyPhotographerCard.toDomain(): Photographer {
    return Photographer(
        id = photographerId,
        name = nickname,
        profileImageUri = profileImage,
        isActive = active == "Y",
        distance = distance,
        photoMoods = photoMoods,
        activeAreas = activeAreas,
    )
}

fun List<NearbyPhotographerCard>.toDomain(): List<Photographer> {
    return map { it.toDomain() }
}
