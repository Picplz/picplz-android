package com.hm.picplz.data.service

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.model.CreateProductRequest
import com.hm.picplz.data.model.ProductDto
import com.hm.picplz.data.model.ProductIdResponse
import com.hm.picplz.data.source.ProductSource
import javax.inject.Inject

interface ProductService {
    suspend fun getPhotographerProducts(photographerId: Long): AppResult<List<ProductDto>>

    suspend fun createProduct(request: CreateProductRequest): AppResult<ProductIdResponse>
}

class ProductServiceImpl
    @Inject
    constructor(
        private val productSource: ProductSource,
    ) : ProductService {
        override suspend fun getPhotographerProducts(photographerId: Long): AppResult<List<ProductDto>> =
            productSource.getPhotographerProducts(photographerId)

        override suspend fun createProduct(request: CreateProductRequest): AppResult<ProductIdResponse> =
            productSource.createProduct(request)
    }
