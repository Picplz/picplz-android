package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.ShootingPackage
import com.hm.picplz.domain.repository.ProductRepository
import javax.inject.Inject

class GetPhotographerProductsUseCase
    @Inject
    constructor(
        private val productRepository: ProductRepository,
    ) {
        suspend operator fun invoke(photographerId: Long): AppResult<List<ShootingPackage>> =
            productRepository.getPhotographerProducts(photographerId)
    }
