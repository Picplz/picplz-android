package com.hm.picplz.di

import com.hm.picplz.data.repository.ProductRepositoryImpl
import com.hm.picplz.data.service.ProductService
import com.hm.picplz.data.service.ProductServiceImpl
import com.hm.picplz.data.source.ProductSource
import com.hm.picplz.data.source.ProductSourceImpl
import com.hm.picplz.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindProductService(productServiceImpl: ProductServiceImpl): ProductService

    @Binds
    @Singleton
    abstract fun bindProductSource(productSourceImpl: ProductSourceImpl): ProductSource
}
