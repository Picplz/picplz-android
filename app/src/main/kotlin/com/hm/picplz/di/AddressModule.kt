package com.hm.picplz.di

import com.hm.picplz.data.repository.AreaRepositoryImpl
import com.hm.picplz.data.repository.LocationRepositoryImpl
import com.hm.picplz.data.service.AddressService
import com.hm.picplz.data.service.AddressServiceImpl
import com.hm.picplz.data.source.AddressSource
import com.hm.picplz.data.source.AddressSourceImpl
import com.hm.picplz.domain.repository.AreaRepository
import com.hm.picplz.domain.repository.LocationRepository
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

    @Binds
    @Singleton
    abstract fun bindAreaRepository(areaRepositoryImpl: AreaRepositoryImpl): AreaRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository
}
