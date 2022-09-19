package com.jarvis.bmihealth.di

import android.app.Application
import androidx.room.Room
import com.jarvis.bmihealth.data.datasource.AppDatabase
import com.jarvis.bmihealth.data.repository.ProfileUserRepositoryImpl
import com.jarvis.bmihealth.domain.repository.ProfileUserRepository
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppComponent {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideAppRepository(appDatabase: AppDatabase): ProfileUserRepository =
        ProfileUserRepositoryImpl(appDatabase.profileUserDao)

    @Provides
    @Singleton
    fun provideProfileUserUseCases(profileUserRepository: ProfileUserRepository): UserProfileUseCase =
        UserProfileUseCase(profileUserRepository)
}