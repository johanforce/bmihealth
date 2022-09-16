package com.jarvis.bmihealth.di

import com.jarvis.bmihealth.di.module.DaoModule
import com.jarvis.bmihealth.di.module.RepoModule
import com.jarvis.bmihealth.di.module.ViewModelModule
import com.jarvis.bmihealth.presentation.bmiother.OtherFragment
import com.jarvis.bmihealth.presentation.home.HomeFragment
import com.jarvis.bmihealth.presentation.main.MainActivity
import com.jarvis.bmihealth.presentation.profile.ProfileFragment
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DaoModule::class, RepoModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(fragment: HomeFragment)
    fun inject(fragment: OtherFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(activity: MainActivity)
    fun inject(activity: RegisterActivity)
}
