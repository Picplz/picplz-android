package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.model.PhotographerDetailDto
import com.hm.picplz.data.model.PhotographerInfo
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
        instagram = "@photographer_$photographerId",
        portfolioPhotos = List(4) { "https://picsum.photos/200/200?random=${photographerId * 10 + it}" },
    )
}

fun List<NearbyPhotographerCard>.toDomain(): List<Photographer> {
    return map { it.toDomain() }
}

fun PhotographerDetailDto.toPhotographerInfo(): PhotographerInfo {
    return PhotographerInfo(
        id = photographerId.toInt(),
        name = nickname,
        socialAccount = instagram,
        infoText = introduction ?: "",
        isActive = active == "Y",
        isBookable = active == "Y",
        isFollow = isFollowing == "Y",
        followCount = followers ?: 0,
        profileImageUri = profileImage ?: "",
        workingArea = area?.mapNotNull { it.name } ?: emptyList(),
        keyword = photoMoods ?: emptyList(),
        equipment = emptyList(),
        photoPortfolios = emptyList(),
    )
}
