package com.hm.picplz.data.model

data class ProductDto(
    val productId: Long,
    val photographerId: Long?,
    val title: String?,
    val name: String?,
    val price: Int?,
    val shootPrice: Int?,
    val description: String?,
    val shootingTime: String?,
    val shootDuration: Int?,
    val imageUrl: String?,
    val otherDetails: String?,
    val productPhotos: List<String>?,
)

data class PortfolioDto(
    val portfolioId: Long,
    val photos: List<PortfolioPhotoDto>?,
    val location: String?,
    val uploadDate: String?,
    val scrapCount: Long?,
    val scrapYN: Boolean?,
)

data class PortfolioPhotoDto(
    val portfolioPhotoId: Long?,
    val image: String?,
    val photoOrder: Int?,
)
