package com.jarvis.bmihealth.presentation.onboarding

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.domain.model.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    BaseViewModel() {

    var profileUsers = MutableLiveData<List<ProfileUserModel>>()

    fun getProfile() {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                profileUsers.postValue(userProfileUseCase.getAllUserProfile())
            }
        }
    }
}
