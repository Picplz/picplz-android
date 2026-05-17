package com.hm.picplz.data.source

import com.hm.picplz.data.api.ProductApi
import com.hm.picplz.data.model.CreateProductRequest
import com.hm.picplz.data.model.ProductDto
import com.hm.picplz.data.model.ProductIdResponse
import com.hm.picplz.data.util.safeApiCall
import javax.inject.Inject

interface ProductSource {
    suspend fun getPhotographerProducts(photographerId: Long): Result<List<ProductDto>>

    suspend fun createProduct(request: CreateProductRequest): Result<ProductIdResponse>
}

class ProductSourceImpl
    @Inject
    constructor(
        private val productApi: ProductApi,
    ) : ProductSource {
        override suspend fun getPhotographerProducts(photographerId: Long): Result<List<ProductDto>> =
            safeApiCall { productApi.getPhotographerProducts(photographerId) }

        override suspend fun createProduct(request: CreateProductRequest): Result<ProductIdResponse> =
            safeApiCall { productApi.createProduct(request) }
    }
