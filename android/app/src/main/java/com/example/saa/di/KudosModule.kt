package com.example.saa.di

import com.example.saa.data.remote.source.StorageDataSource
import com.example.saa.data.repository.KudosRepositoryImpl
import com.example.saa.data.repository.ProfileRepositoryImpl
import com.example.saa.data.repository.UserStatsRepositoryImpl
import com.example.saa.domain.repository.KudosRepository
import com.example.saa.domain.repository.ProfileRepository
import com.example.saa.domain.repository.UserStatsRepository
import com.example.saa.domain.usecase.ImageUploader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KudosModule {

    @Binds
    @Singleton
    abstract fun bindKudosRepository(impl: KudosRepositoryImpl): KudosRepository

    @Binds
    @Singleton
    abstract fun bindUserStatsRepository(impl: UserStatsRepositoryImpl): UserStatsRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindImageUploader(impl: StorageDataSource): ImageUploader
}
