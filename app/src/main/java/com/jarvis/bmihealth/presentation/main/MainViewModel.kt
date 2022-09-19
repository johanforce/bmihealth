package com.jarvis.bmihealth.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.utilx.Constant.MALE
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

    var tempFrag = MutableLiveData<Int>()

//    fun insertProfile() {
//        launch(Dispatchers.Main) {
//            withContext(Dispatchers.IO) {
//                userProfileUseCase.insertProfileUser(
//                    ProfileUser("Nguyen", "Thang", MALE, 931194000000, 23, 70.0, 165.0, "ALa", "Vn")
//                )
//            }
//        }
//    }
}
