package com.hm.picplz.data.repository

import com.hm.picplz.data.mapper.toRequest
import com.hm.picplz.data.mapper.toShootingPackage
import com.hm.picplz.data.service.ProductService
import com.hm.picplz.domain.model.CreateProductCommand
import com.hm.picplz.domain.model.ShootingPackage
import com.hm.picplz.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl
    @Inject
    constructor(
        private val productService: ProductService,
    ) : ProductRepository {
        override suspend fun getPhotographerProducts(photographerId: Long): Result<List<ShootingPackage>> =
            productService.getPhotographerProducts(photographerId).map { products ->
                products.map { it.toShootingPackage() }
            }

        override suspend fun createProduct(command: CreateProductCommand): Result<Long> =
            productService.createProduct(command.toRequest()).map { it.id }
    }
