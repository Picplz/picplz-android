package com.hm.picplz.domain.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.CreateProductCommand
import com.hm.picplz.domain.model.ShootingPackage

interface ProductRepository {
    suspend fun getPhotographerProducts(photographerId: Long): AppResult<List<ShootingPackage>>

    suspend fun createProduct(command: CreateProductCommand): AppResult<Long>
}
