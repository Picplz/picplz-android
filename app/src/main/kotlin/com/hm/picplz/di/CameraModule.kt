package com.hm.picplz.di

import com.hm.picplz.data.repository.CameraRepositoryImpl
import com.hm.picplz.data.service.CameraService
import com.hm.picplz.data.service.CameraServiceImpl
import com.hm.picplz.data.source.CameraSource
import com.hm.picplz.data.source.CameraSourceImpl
import com.hm.picplz.domain.repository.CameraRepository
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

    @Binds
    @Singleton
    abstract fun bindCameraRepository(cameraRepositoryImpl: CameraRepositoryImpl): CameraRepository
}
