package com.jarvis.bmihealth.presentation.home

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    BaseViewModel() {
    var profileUser = MutableLiveData<List<ProfileUserModel>>()
    fun getProfile() {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                profileUser.postValue(userProfileUseCase.getAllUserProfile())
            }
        }
    }

    fun insertProfile() {

//        launch(Dispatchers.Main) {
//            withContext(Dispatchers.IO) {
//                userProfileUseCase.insertProfileUser(
//                    ProfileUserModel(
//                        "Nguyen",
//                        "Thang",
//                        MALE,
//                        931194000000,
//                        23,
//                        70.0,
//                        165.0,
//                        "ALa",
//                        "Vn"
//                    )
//                )
//            }
//        }
    }
}
