package com.jarvis.bmihealth.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import com.jarvis.bmihealth.presentation.base.ProfileUserViewModel
import com.jarvis.bmihealth.presentation.utilx.ActivityEnum
import com.jarvis.bmihealth.presentation.utilx.TypeUnit
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ProfileUserViewModel() {
    val isShowHealthIndex = MutableLiveData<Boolean>().apply { value = false }
    var tempFrag = MutableLiveData<Int>()

    fun onClickFrag(temp: Int) {
        if (temp == 0) {
            tempFrag.value = 0
        } else if (temp == 1) {
            tempFrag.value = 1
        }
    }

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
