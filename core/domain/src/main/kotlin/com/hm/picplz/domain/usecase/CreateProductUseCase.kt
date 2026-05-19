package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.model.CreateProductCommand
import com.hm.picplz.domain.repository.ProductRepository
import javax.inject.Inject

class CreateProductUseCase
    @Inject
    constructor(
        private val productRepository: ProductRepository,
    ) {
        suspend operator fun invoke(command: CreateProductCommand): Result<Long> =
            productRepository.createProduct(command)
    }
