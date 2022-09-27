package com.jarvis.bmihealth.presentation.selectmode.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import com.jarvis.bmihealth.presentation.selectmode.ThemeHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectModeViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    BaseViewModel() {

    val themeMode = MutableLiveData<Int>()

    var profileUsers = MutableLiveData<List<ProfileUserModel>>()
    var profileUser = ProfileUserModel()

    fun getProfile() {
        viewModelScope.launch {
            profileUsers.value = userProfileUseCase.getAllUserProfile()
        }
    }

    fun updateProfile(userModel: ProfileUserModel) {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                userProfileUseCase.updateProfileUser(userModel)
            }
        }
    }

    fun updateDataView() {
        profileUser = profileUsers.value?.firstOrNull() ?: ProfileUserModel()
        themeMode.value = profileUser.themeMode
    }

    fun selectMode(mode: Int) {
        if (mode == this.themeMode.value) {
            return
        }
        ThemeHelper.applyTheme(mode)
        themeMode.value = mode
        profileUser.themeMode = mode
        updateProfile(profileUser)
    }

}