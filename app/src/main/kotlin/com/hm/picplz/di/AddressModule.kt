package com.hm.picplz.di

import com.hm.picplz.data.service.AddressService
import com.hm.picplz.data.service.AddressServiceImpl
import com.hm.picplz.data.source.AddressSource
import com.hm.picplz.data.source.AddressSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AddressModule {
    @Binds
    @Singleton
    abstract fun bindAddressService(addressServiceImpl: AddressServiceImpl): AddressService

    @Binds
    @Singleton
    abstract fun bindAddressSource(addressSourceImpl: AddressSourceImpl): AddressSource
}
