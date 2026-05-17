package com.hm.picplz.domain.repository

import com.hm.picplz.domain.model.CreateProductCommand
import com.hm.picplz.domain.model.ShootingPackage

interface ProductRepository {
    suspend fun getPhotographerProducts(photographerId: Long): Result<List<ShootingPackage>>

    suspend fun createProduct(command: CreateProductCommand): Result<Long>
}
