package com.jarvis.bmihealth.presentation.home

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    BaseViewModel() {

    val isShowHealthIndex = MutableLiveData<Boolean>().apply { value = false }
}
