package com.jarvis.bmihealth.di

import android.app.Application
import androidx.room.Room
import com.jarvis.bmihealth.data.data_source.AppDatabase
import com.jarvis.bmihealth.data.repository.ProfileUserRepositoryImpl
import com.jarvis.bmihealth.domain.repository.ProfileUserRepository
import com.jarvis.bmihealth.domain.use_case.UserProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideNoteRepository(noteDatabase: AppDatabase): ProfileUserRepository =
        ProfileUserRepositoryImpl(noteDatabase.profileUserDao)

    @Provides
    @Singleton
    fun provideNoteUseCases(noteRepository: ProfileUserRepository): UserProfileUseCase =
        UserProfileUseCase(noteRepository)
}