package com.jarvis.bmihealth.presentation.main

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.domain.use_case.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class MainViewModel :
    BaseViewModel() {
    var tempFrag = MutableLiveData<Int>()
}