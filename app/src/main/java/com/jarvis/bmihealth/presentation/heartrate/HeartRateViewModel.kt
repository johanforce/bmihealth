package com.jarvis.bmihealth.presentation.heartrate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jarvis.bmihealth.domain.model.model.HeartRateModel
import com.jarvis.bmihealth.domain.usecase.HeartRateUseCase
import com.jarvis.bmihealth.presentation.base.ProfileUserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeartRateViewModel @Inject constructor() : ProfileUserViewModel() {

    @Inject
    lateinit var heartRateUseCase: HeartRateUseCase

    var heartRateModel = MutableLiveData<HeartRateModel>()

    fun getLastResultHeartRate() {
        viewModelScope.launch {
            heartRateModel.value = heartRateUseCase.getLastResultHeartRate()
        }
    }
}

