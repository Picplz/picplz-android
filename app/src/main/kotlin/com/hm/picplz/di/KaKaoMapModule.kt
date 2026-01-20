package com.hm.picplz.di

import com.hm.picplz.data.service.KakaoMapService
import com.hm.picplz.data.service.KakaoMapServiceImpl
import com.hm.picplz.data.source.KakaoMapSource
import com.hm.picplz.data.source.KakaoMapSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KaKaoMapModule {
    @Binds
    @Singleton
    abstract fun bindKaKaoMapService(kaKaoMapServiceImpl: KakaoMapServiceImpl): KakaoMapService

    @Binds
    @Singleton
    abstract fun bindAddressSource(kaKaoMapSourceImpl: KakaoMapSourceImpl): KakaoMapSource
}
