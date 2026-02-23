package com.hm.picplz.di

import com.hm.picplz.data.service.CameraService
import com.hm.picplz.data.service.CameraServiceImpl
import com.hm.picplz.data.source.CameraSource
import com.hm.picplz.data.source.CameraSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CameraModule {
    @Binds
    @Singleton
    abstract fun bindCameraService(cameraServiceImpl: CameraServiceImpl): CameraService

    @Binds
    @Singleton
    abstract fun bindCameraSource(cameraSourceImpl: CameraSourceImpl): CameraSource
}
