package com.hm.picplz.di

import com.hm.picplz.data.service.CustomerService
import com.hm.picplz.data.service.CustomerServiceImpl
import com.hm.picplz.data.source.CustomerSource
import com.hm.picplz.data.source.CustomerSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CustomerModule {
    @Binds
    @Singleton
    abstract fun bindCustomerService(customerServiceImpl: CustomerServiceImpl): CustomerService

    @Binds
    @Singleton
    abstract fun bindCustomerSource(customerSourceImpl: CustomerSourceImpl): CustomerSource
}
