package com.jarvis.bmihealth.presentation.bmiother

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class OtherViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    ViewModel(), CoroutineScope {

    val empty = MutableLiveData<Boolean>().apply { value = false }

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val dataLoaded = MutableLiveData<Boolean>().apply { value = false }

    val toastMessage = MutableLiveData<String>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job: kotlinx.coroutines.Job = kotlinx.coroutines.SupervisorJob()

    override fun onCleared() {
        super.onCleared()
        job.cancel() // huỷ bỏ job
    }
}