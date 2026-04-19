package com.hm.picplz.di

import com.hm.picplz.data.repository.MemberRepositoryImpl
import com.hm.picplz.data.service.MemberService
import com.hm.picplz.data.service.MemberServiceImpl
import com.hm.picplz.data.source.MemberSource
import com.hm.picplz.data.source.MemberSourceImpl
import com.hm.picplz.domain.repository.MemberRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MemberModule {
    @Binds
    @Singleton
    abstract fun bindMemberRepository(memberRepositoryImpl: MemberRepositoryImpl): MemberRepository

    @Binds
    @Singleton
    abstract fun bindMemberService(memberServiceImpl: MemberServiceImpl): MemberService

    @Binds
    @Singleton
    abstract fun bindMemberSource(memberSourceImpl: MemberSourceImpl): MemberSource
}
