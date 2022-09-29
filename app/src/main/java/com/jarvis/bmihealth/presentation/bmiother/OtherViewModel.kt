package com.jarvis.bmihealth.presentation.bmiother

import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherViewModel @Inject constructor() : BaseViewModel() {
    lateinit var userProfileUseCase: UserProfileUseCase
}