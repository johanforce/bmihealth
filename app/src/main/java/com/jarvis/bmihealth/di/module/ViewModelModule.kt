package com.jarvis.bmihealth.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jarvis.bmihealth.presentation.base.ViewModelFactory
import com.jarvis.bmihealth.presentation.bmiother.OtherViewModel
import com.jarvis.bmihealth.presentation.home.HomeViewModel
import com.jarvis.bmihealth.presentation.main.MainViewModel
import com.jarvis.bmihealth.presentation.onboarding.OnBoardingViewModel
import com.jarvis.bmihealth.presentation.profile.ProfileViewModel
import com.jarvis.bmihealth.presentation.register.RegisterViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
@Suppress("unused")
abstract class ViewModelModule {
    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OtherViewModel::class)
    abstract fun bindOtherViewModel(viewModel: OtherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnBoardingViewModel::class)
    abstract fun bindOnBoardingViewModel(viewModel: OnBoardingViewModel): ViewModel
}