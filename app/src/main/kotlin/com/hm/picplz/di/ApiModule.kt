package com.hm.picplz.di

import com.hm.picplz.data.api.KakaoMapApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OurBackendRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @KakaoRetrofit
    fun provideKakaoRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideKakaoMapApi(@KakaoRetrofit retrofit: Retrofit): KakaoMapApi {
        return retrofit.create(KakaoMapApi::class.java)
    }

    @Provides
    @Singleton
    @OurBackendRetrofit
    fun provideOurBackendRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.ourservice.com/")  // 자체 백엔드 URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}