package com.jarvis.bmihealth.presentation.selectmode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectModeViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    BaseViewModel() {

    val themeMode = MutableLiveData<Int>()

    var profileUsers = MutableLiveData<List<ProfileUserModel>>()
    var profileUser = ProfileUserModel()

    var isChangeThemeMode = MutableLiveData<Boolean>()

    fun getProfile() {
        viewModelScope.launch {
            profileUsers.value = userProfileUseCase.getAllUserProfile()
        }
    }

    fun updateProfile(userModel: ProfileUserModel) {
        viewModelScope.launch {
            userProfileUseCase.updateProfileUser(userModel)
            isChangeThemeMode.value = true
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
        profileUser.themeMode = mode
        updateProfile(profileUser)
    }
}
