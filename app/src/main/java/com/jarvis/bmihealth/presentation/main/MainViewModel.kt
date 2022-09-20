package com.jarvis.bmihealth.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.utilx.Constant.MALE
import com.jarvis.bmihealth.presentation.utilx.DeviceUtil
import com.jarvis.bmihealth.presentation.utilx.TypeUnit.Companion.METRIC
import com.jarvis.heathcarebmi.utils.BMILevelAdult
import com.jarvis.heathcarebmi.utils.BMILevelChild
import com.jarvis.heathcarebmi.utils.BMIUtils
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("unused")
@HiltViewModel
class MainViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    ViewModel(), CoroutineScope {

    val empty = MutableLiveData<Boolean>().apply { value = false }

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val dataLoaded = MutableLiveData<Boolean>().apply { value = false }

    val toastMessage = MutableLiveData<String>()

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = Dispatchers.Main + job

    private val job: kotlinx.coroutines.Job = kotlinx.coroutines.SupervisorJob()

    override fun onCleared() {
        super.onCleared()
        job.cancel() // huỷ bỏ job
    }

    var profileUsers = MutableLiveData<List<ProfileUserModel>>()
    var profileUser = ProfileUserModel()
    var isKmSetting = false
    var tempFrag = MutableLiveData<Int>()

    fun onClickFrag(temp: Int) {
        if (temp == 0) {
            tempFrag.value = 0
        } else if (temp == 1) {
            tempFrag.value = 1
        }
    }


    fun getProfile() {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                profileUsers.postValue(userProfileUseCase.getAllUserProfile())
                profileUser= profileUsers.value?.firstOrNull()?: ProfileUserModel()
                isKmSetting = profileUser.unit == METRIC
            }
        }
    }

    fun getBMR(): Int{
        return HealthIndexUtils.getBmr(profileUser.weight, profileUser.height, profileUser.birthday, profileUser.gender).toInt()
    }

    fun getHealthyWeight(): HealthIndexUtils.HealthyWeight {
        return HealthIndexUtils.getHealthyWeight(
            profileUser.birthday,
            profileUser.gender,
            profileUser.height
        )
    }

    fun getBMI():Double{
        return HealthIndexUtils.getBmi(
            profileUser.birthday,
            System.currentTimeMillis(),
            profileUser.gender,
            profileUser.weight,
            profileUser.height
        )
    }

    fun getBodyFat():Double{
        return HealthIndexUtils.getBodyFatPercentage(
            getBMI(),
            profileUser.gender,
            profileUser.birthday,
        )
    }

    fun getCalories():Int{
        return HealthIndexUtils.getCaloRequired(
            HealthIndexUtils.getTdee(
                getBMR().toDouble(),
                profileUser.activityLevel?:0
            ),
            profileUser.goal?:0
        ).toInt()
    }

    fun getLevelBMI(): Pair<Boolean, Int>{
        val isAdult = !HealthIndexUtils.isChild(profileUser.birthday)

        return if(isAdult){
            true to levelBMIAdult(getBMI())
        }else{
            false to levelBMChild(getBMI())
        }
    }

    fun levelBMIAdult(bmi: Double): Int{
        return if(bmi < 16.0){
            BMILevelAdult.BMI_1
        }else if(bmi >= 16 && bmi <18.5){
            BMILevelAdult.BMI_2
        }else if(bmi >= 18.5 && bmi <25){
            BMILevelAdult.BMI_3
        }else if(bmi >= 25 && bmi <30){
            BMILevelAdult.BMI_4
        }else if(bmi >= 30 && bmi <35){
            BMILevelAdult.BMI_5
        }else {
            BMILevelAdult.BMI_6
        }
    }

    fun levelBMChild(bmi: Double): Int{
        return if(bmi < 3.0){
            BMILevelChild.BMI_1
        }else if(bmi >= 3.0 && bmi <15.0){
            BMILevelChild.BMI_2
        }else if(bmi >= 15 && bmi <85){
            BMILevelChild.BMI_3
        }else if(bmi >= 85 && bmi <97){
            BMILevelChild.BMI_4
        }else {
            BMILevelAdult.BMI_5
        }
    }
}
