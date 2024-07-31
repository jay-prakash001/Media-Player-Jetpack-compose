package com.jayvideoplayer.presentation.di

import com.jayvideoplayer.data.repo.VideoRepoImpl
import com.jayvideoplayer.domain.repo.VideoAppRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun provideRepoImpl(): VideoAppRepo {
        return VideoRepoImpl()
    }

}