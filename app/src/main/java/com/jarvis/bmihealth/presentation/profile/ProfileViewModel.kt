package com.jarvis.bmihealth.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import com.jarvis.bmihealth.presentation.utilx.TypeUnit
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("unused")
@HiltViewModel
class ProfileViewModel @Inject constructor() : BaseViewModel() {
    lateinit var userProfileUseCase: UserProfileUseCase

    var profileUsers = MutableLiveData<List<ProfileUserModel>>()
    var profileUser = ProfileUserModel()
    var isKmSetting = false
    var isChild = false
    var tempFrag = MutableLiveData<Int>()

    fun onClickFrag(temp: Int) {
        if (temp == 0) {
            tempFrag.value = 0
        } else if (temp == 1) {
            tempFrag.value = 1
        }
    }

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
        isKmSetting = profileUser.unit == TypeUnit.METRIC
        isChild = HealthIndexUtils.isChild(profileUser.birthday)
    }

    fun getBMR(): Int {
        return HealthIndexUtils.getBmr(
            profileUser.weight,
            profileUser.height,
            profileUser.birthday,
            profileUser.gender
        ).toInt()
    }

    fun getHealthyWeight(): HealthIndexUtils.HealthyWeight {
        return HealthIndexUtils.getHealthyWeight(
            profileUser.birthday,
            profileUser.gender,
            profileUser.height
        )
    }

    fun getBMI(): Double {
        return HealthIndexUtils.getBmi(
            profileUser.birthday,
            System.currentTimeMillis(),
            profileUser.gender,
            profileUser.weight,
            profileUser.height
        )
    }

    fun getBodyFat(): Double {
        return HealthIndexUtils.getBodyFatPercentage(
            getBMI(),
            profileUser.gender,
            profileUser.birthday,
        )
    }

    fun getCalories(): Int {
        return HealthIndexUtils.getCaloRequired(
            HealthIndexUtils.getTdee(
                getBMR().toDouble(),
                profileUser.activityLevel ?: 0
            ),
            profileUser.goal ?: 0
        ).toInt()
    }
}
