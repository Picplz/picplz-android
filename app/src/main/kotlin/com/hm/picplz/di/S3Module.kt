package com.hm.picplz.di

import com.hm.picplz.data.service.S3Service
import com.hm.picplz.data.service.S3ServiceImpl
import com.hm.picplz.data.source.S3Source
import com.hm.picplz.data.source.S3SourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class S3Module {
    @Binds
    @Singleton
    abstract fun bindS3Service(s3ServiceImpl: S3ServiceImpl): S3Service

    @Binds
    @Singleton
    abstract fun bindS3Source(s3SourceImpl: S3SourceImpl): S3Source
}
