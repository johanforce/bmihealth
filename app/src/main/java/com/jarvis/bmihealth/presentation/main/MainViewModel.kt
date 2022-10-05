package com.jarvis.bmihealth.presentation.main

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.presentation.base.ProfileUserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("unused")
@HiltViewModel
class MainViewModel @Inject constructor() : ProfileUserViewModel() {
    var tempFrag = MutableLiveData<Int>()

    fun onClickFrag(temp: Int) {
        if (temp == 0) {
            tempFrag.value = 0
        } else if (temp == 1) {
            tempFrag.value = 1
        }
    }
}

