package com.jarvis.bmihealth.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@Suppress("unused")
@HiltViewModel
class ProfileViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    BaseViewModel(){


}