package com.hm.picplz.di

import com.hm.picplz.BuildConfig
import com.hm.picplz.data.provider.ConfigProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Singleton
    fun provideConfigProvider(): ConfigProvider = object : ConfigProvider {
        override val devGuestToken: String = BuildConfig.DEV_GUEST_TOKEN
        override val devUserToken: String = BuildConfig.DEV_USER_TOKEN
        override val kakaoRestApiKey: String = BuildConfig.KAKAO_REST_API_KEY
    }
}
