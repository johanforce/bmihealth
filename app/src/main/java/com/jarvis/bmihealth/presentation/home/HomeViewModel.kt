package com.jarvis.bmihealth.presentation.home

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.repository.ProfileUserRepository
import com.jarvis.bmihealth.domain.use_case.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase): BaseViewModel() {
    var profileUser = MutableLiveData<ProfileUser>()
    fun getProfile(){
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                profileUser.postValue(userProfileUseCase.getUserProfile(1))
            }
        }
    }
}