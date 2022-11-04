@file:Suppress("DEPRECATION", "SpellCheckingInspection")

package com.jarvis.bmihealth.presentation.heartrate.diary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jarvis.bmihealth.domain.model.model.HeartRateModel
import com.jarvis.bmihealth.domain.usecase.HeartRateUseCase
import com.jarvis.bmihealth.presentation.base.ProfileUserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeartRateDiaryViewModel @Inject constructor() : ProfileUserViewModel() {
    @Inject
    lateinit var heartRateUseCase: HeartRateUseCase

    val listHeartRate = MutableLiveData<List<HeartRateModel?>>()

    fun getHeartRateInTime(startTime: Long, endTime: Long) {
        viewModelScope.launch {
            listHeartRate.value = heartRateUseCase.getAllHeartRateInTime(startTime, endTime)
        }
    }
}


