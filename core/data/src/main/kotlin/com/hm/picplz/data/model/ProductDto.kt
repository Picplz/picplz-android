package com.hm.picplz.data.model

data class ProductDto(
    val productId: Long,
    val title: String?,
    val price: Int?,
    val description: String?,
    val shootingTime: String?,
    val imageUrl: String?,
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
