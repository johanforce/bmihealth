package com.jarvis.bmihealth.presentation.home

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.presentation.base.ProfileUserViewModel
import com.jarvis.bmihealth.common.enums.ActivityEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ProfileUserViewModel() {
    val isShowHealthIndex = MutableLiveData<Boolean>().apply { value = false }

    fun updateTitleCalories(activityLevel: Int): String {
        return when (activityLevel) {
            ActivityEnum.SEDENTARY.index -> {
                ActivityEnum.SEDENTARY.title
            }
            ActivityEnum.LIGHT_ACTIVE.index -> {
                ActivityEnum.LIGHT_ACTIVE.title
            }
            ActivityEnum.MODERATELY.index -> {
                ActivityEnum.MODERATELY.title
            }
            ActivityEnum.VERY_ACTIVE.index -> {
                ActivityEnum.VERY_ACTIVE.title
            }
            ActivityEnum.EXTREMELY_ACTIVE.index -> {
                ActivityEnum.EXTREMELY_ACTIVE.title
            }
            else -> ActivityEnum.MODERATELY.title
        }
    }
}
