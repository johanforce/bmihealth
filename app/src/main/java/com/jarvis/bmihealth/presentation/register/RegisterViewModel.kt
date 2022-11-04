package com.jarvis.bmihealth.presentation.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jarvis.bmihealth.domain.model.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    ViewModel(), CoroutineScope {

    val empty = MutableLiveData<Boolean>().apply { value = false }

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val dataLoaded = MutableLiveData<Boolean>().apply { value = false }

    val toastMessage = MutableLiveData<String>()

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = Dispatchers.Main + job

    private val job: kotlinx.coroutines.Job = kotlinx.coroutines.SupervisorJob()

    var profileUsers = MutableLiveData<List<ProfileUserModel>>()
    var profileUser = ProfileUserModel()

    var isInsertProfile = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        job.cancel() // huỷ bỏ job
    }

    fun getProfile() {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                profileUsers.postValue(userProfileUseCase.getAllUserProfile())
            }
        }
    }

    fun insertProfile(userModel: ProfileUserModel) {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                userProfileUseCase.insertProfileUser(userModel)
                isInsertProfile.postValue(true)
            }
        }
    }

    fun updateProfile(userModel: ProfileUserModel) {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                userProfileUseCase.updateProfileUser(userModel)
                isInsertProfile.postValue(true)
            }
        }
    }
}
