@file:Suppress("MemberVisibilityCanBePrivate")

package com.jarvis.bmihealth.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.common.enums.GenderEnum.MALE
import com.jarvis.bmihealth.common.enums.TypeUnits
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("unused")
@HiltViewModel
open class ProfileUserViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var userProfileUseCase: UserProfileUseCase

    var profileUsers = MutableLiveData<List<ProfileUserModel>>()
    var profileUser = ProfileUserModel()
    var isKmSetting = false
    var isChild = false
    var isMale = false

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
        isKmSetting = profileUser.unit == TypeUnits.METRIC.index
        isChild = HealthIndexUtils.isChild(profileUser.birthday)
        isMale = profileUser.gender == MALE.index
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
