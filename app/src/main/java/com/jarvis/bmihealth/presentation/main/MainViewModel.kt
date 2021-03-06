package com.jarvis.bmihealth.presentation.main

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.repository.ProfileUserRepository
import com.jarvis.bmihealth.domain.use_case.UserProfileUseCase
import com.jarvis.bmihealth.presentation.utilx.Constant.Companion.MALE
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase): BaseViewModel() {
    var tempFrag = MutableLiveData<Int>()

    fun insertProfile(){
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                userProfileUseCase.insertProfileUser(
                    ProfileUser("Nguyen", "Thang", MALE,931194000000, 23, 70.0, 165.0, "ALa", "Vn")
                )
            }
        }
    }
}