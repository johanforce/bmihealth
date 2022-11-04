@file:Suppress("unused")

package com.jarvis.bmihealth.presentation.detail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.domain.model.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import com.jarvis.bmihealth.common.enums.GoalEnum
import com.jarvis.bmihealth.common.enums.TypeUnits
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    BaseViewModel() {

    var isKmSetting = true
    val arrayListIndex = arrayListOf(0, 1, 2)
    val arrayListStatus = arrayListOf(
        R.string.healthy_weight_status_3,
        R.string.healthy_weight_status_2,
        R.string.healthy_weight_status_1
    )
    val arrayListStatusDes = arrayListOf(
        R.string.healthy_weight_status_des_3,
        R.string.healthy_weight_status_des_2,
        R.string.healthy_weight_status_des_1
    )
    val arrayListStatusColor =
        arrayListOf(R.color.bmi_level1, R.color.bmi_level2, R.color.bmi_level4)

    var minWeight = 0.0
    var maxWeight = 0.0
    var userWeight = 0.0

    var profileUsers = MutableLiveData<List<ProfileUserModel>>()
    var profileUser = ProfileUserModel()
    private var isChild = false

    fun getProfile() {
        viewModelScope.launch {
            profileUsers.value = userProfileUseCase.getAllUserProfile()
        }
    }

    fun updateDataView() {
        profileUser = profileUsers.value?.firstOrNull() ?: ProfileUserModel()
        isKmSetting = profileUser.unit == TypeUnits.METRIC.index
        isChild = HealthIndexUtils.isChild(profileUser.birthday)
        setDataWeight()
    }

    private fun setDataWeight() {
        val healthyWeight = HealthIndexUtils.getHealthyWeight(
            profileUser.birthday,
            profileUser.gender, profileUser.height
        )
        minWeight = healthyWeight.healthyWeightFrom
        maxWeight = healthyWeight.healthyWeightTo
        userWeight = profileUser.weight
    }

    fun getTextGoal(goal: Int, context: Context): String {
        when (goal) {
            GoalEnum.WEIGHT_LOSS_III.index -> {
                return context.getString(R.string.onboarding_strict_loos)
            }
            GoalEnum.WEIGHT_LOSS_II.index -> {
                return context.getString(R.string.onboarding_mormal_weight)
            }
            GoalEnum.WEIGHT_LOSS_I.index -> {
                return context.getString(R.string.onboarding_comfortable)
            }
            GoalEnum.MAINTAIN_WEIGHT.index -> {
                return context.getString(R.string.onboarding_maintain)
            }
            GoalEnum.WEIGHT_GAIN_I.index -> {
                return context.getString(R.string.onboarding_normal)
            }
            GoalEnum.WEIGHT_GAIN_II.index -> {
                return context.getString(R.string.onboarding_strict)
            }
        }
        return ""
    }

    fun getTextDescriptionGoal(goal: Int, context: Context): String {
        when (goal) {
            GoalEnum.WEIGHT_LOSS_III.index -> {
                return context.getString(R.string.all_strict_loss_description)
            }
            GoalEnum.WEIGHT_LOSS_II.index -> {
                return context.getString(R.string.all_normal_loss_description)
            }
            GoalEnum.WEIGHT_LOSS_I.index -> {
                return context.getString(R.string.all_comfortable_loss_description)
            }
            GoalEnum.MAINTAIN_WEIGHT.index -> {
                return context.getString(R.string.all_maintain_weight_description)
            }
            GoalEnum.WEIGHT_GAIN_I.index -> {
                return context.getString(R.string.all_normal_gain_description)
            }
            GoalEnum.WEIGHT_GAIN_II.index -> {
                return context.getString(R.string.all_strict_gain_description)
            }
        }
        return ""
    }

    fun getCaloriesUser(): Int {
        val tde = HealthIndexUtils.getTdee(getBmrUser(), profileUser.activityLevel ?: 0)
        return HealthIndexUtils.getCaloRequired(
            tde,
            profileUser.goal ?: 0
        ).toInt()
    }

    fun getBmrUser(): Double {
        return HealthIndexUtils.getBmr(
            profileUser.weight,
            profileUser.height,
            profileUser.birthday,
            profileUser.gender
        )
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
}
