package com.jarvis.bmihealth.presentation.onboarding

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import com.well.being.MainApplication
import com.well.being.base.ui.BaseViewModel
import com.well.being.onboarding.model.Gender
import com.well.being.onboarding.model.OtherProfile
import com.well.being.onboarding.model.TypeUnit
import com.well.being.pref.AppPreference
import com.well.being.pref.AppPreferenceKey
import com.well.being.room.database.AppDAO
import com.well.being.room.database.AppDatabase
import com.well.being.room.entity.*
import com.well.being.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase):
    BaseViewModel() {
    var isClickWeight = MutableLiveData(false)
    var isInsertAllSuccess = MutableLiveData<Boolean>()
    var profileUser = OtherProfile()
    var isMale = MutableLiveData(profileUser.gender == Gender.MALE)
    var isMetric = MutableLiveData(profileUser.unit == TypeUnit.METRIC)
    var isShowActivityLevelHelp = MutableLiveData(false)
    var appPreference: AppPreference? = AppPreference.getInstance()
    lateinit var appDAO: AppDAO

    fun initView() {
        val database = AppDatabase.getDatabase(MainApplication.applicationContext())
        this.appDAO = database.getAppDAO()
    }

    fun onClickGender(gender: Int) {
        isMale.value = (gender == Gender.MALE)
    }

    fun onClickUnit(unit: Int) {
        isMetric.value = (unit == TypeUnit.METRIC)
    }

    fun onClickWeight() {
        isClickWeight.value = true
    }

    fun onClickActivityLevelHelpView() {
        isShowActivityLevelHelp.value = false
    }

    fun insertData(otherProfile: OtherProfile) {
        launch(Dispatchers.Main) {
            val userProfile = UserProfile(true,
                otherProfile.firstName.toString(),
                otherProfile.lastName.toString(),
                null,
                "",
                otherProfile.birthday,
                otherProfile.gender,
                otherProfile.heightInCentimeters,
                otherProfile.weightInKilograms,
                otherProfile.unit,
                otherProfile.userType, "", "", 0L,
                otherProfile.goal!!,
                otherProfile.activityLevel
            )
            val userId = withContext(Dispatchers.IO) {
                appDAO.insertUserProfile(userProfile)
            }

            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH] + 1
            val day = calendar[Calendar.DAY_OF_MONTH]
            val stringTimeCurrent =
                year.toString() + String.format("%02d", month) + String.format("%02d", day)
            val heightSummary = HeightSummary(
                userId,
                stringTimeCurrent.toInt(),
                calendar.timeInMillis,
                otherProfile.heightInCentimeters,
                null,
                null,
                null
            )
            val weightSummary = WeightSummary(
                userId,
                stringTimeCurrent.toInt(),
                calendar.timeInMillis,
                otherProfile.weightInKilograms,
                null,
                null,
                null
            )

            val userSync = UserSync()
            userSync.userId = userProfile.id
            userSync.userLevel = if (userProfile.isPrimary) 1 else 0

            val weightSync = WeightSync()
            weightSync.userId = weightSummary.userId.toInt()
            weightSync.shortDate = weightSummary.shortDate

            val heightSync = HeightSync()
            heightSync.userId = heightSummary.userId.toInt()
            heightSync.shortDate = heightSummary.shortDate

            appDAO.insertWeightAndHeight(heightSummary, weightSummary)
            appDAO.insertUserSync(userSync)
            appDAO.insertHeightSync(heightSync)
            appDAO.insertWeightSync(weightSync)
            
            appPreference!!.put(AppPreferenceKey.KEY_INT_USER_ID, userId.toInt())
            appPreference!!.put(AppPreferenceKey.KEY_IS_INPUT_INFO_SUCCESS, true)
            appPreference!!.put(
                AppPreferenceKey.KEY_BOOLEAN_UNIT_METRIC,
                otherProfile.unit == TypeUnit.METRIC
            )
            isInsertAllSuccess.postValue(true)
        }
    }
}