package com.jarvis.bmihealth.presentation.onboarding

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase):
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
}