@file:Suppress("DEPRECATION", "SpellCheckingInspection")

package com.jarvis.bmihealth.presentation.heartrate.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jarvis.bmihealth.domain.model.model.HeartRateModel
import com.jarvis.bmihealth.domain.usecase.HeartRateUseCase
import com.jarvis.bmihealth.presentation.base.ProfileUserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeartRateResultViewModel @Inject constructor() : ProfileUserViewModel() {
    @Inject
    lateinit var heartRateUseCase: HeartRateUseCase

    var activityLevelHeartRate = 0
    var heartRateModel = HeartRateModel()
    val isSaveSuccess = MutableLiveData<Boolean>()

    fun addHeartRate() {
        viewModelScope.launch {
            heartRateUseCase.insertHeartRate(heartRateModel)
            isSaveSuccess.value = true
        }
    }
}

