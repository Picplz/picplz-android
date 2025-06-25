package com.hm.picplz.di

import com.hm.picplz.data.service.AuthService
import com.hm.picplz.data.service.AuthServiceImpl
import com.hm.picplz.data.source.AuthSource
import com.hm.picplz.data.source.AuthSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthService(
        authServiceImpl: AuthServiceImpl
    ): AuthService

    @Binds
    @Singleton
    abstract fun bindAuthSource(
        authSourceImpl: AuthSourceImpl
    ): AuthSource
}