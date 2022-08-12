package com.jarvis.bmihealth.di.module

import com.jarvis.bmihealth.domain.repository.ProfileUserRepository
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepoModule {
    @Provides
    @Singleton
    fun provideProfileUserUseCases(profileUserRepository: ProfileUserRepository): UserProfileUseCase =
        UserProfileUseCase(profileUserRepository)
}
