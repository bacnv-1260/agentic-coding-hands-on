package com.example.saa.di

import com.example.saa.data.repository.AwardRepositoryImpl
import com.example.saa.data.repository.NotificationRepositoryImpl
import com.example.saa.domain.repository.AwardRepository
import com.example.saa.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindAwardRepository(impl: AwardRepositoryImpl): AwardRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
}
